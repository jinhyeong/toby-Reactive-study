package live;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.Flow.*;

public class PubSub {
	public static void main(String[] args) throws InterruptedException {
		// Publisher  <- Observable
		// Subscriber <- Observer

		// 잘 기억하세요. onSubscribe onNext* (onError | onComplete)?
		// onSubscribe 호출해야 하고, onNext 여러번 호출 할 수 있고, (onError | onComplete) 옵셔널하게 하나만 호출가능

		Iterable<Integer> itr = Arrays.asList(1, 2, 3, 4, 5);

		ExecutorService es = Executors.newCachedThreadPool();

		Publisher p = new Publisher() {
			// 주는 쪽
			@Override
			public void subscribe(Subscriber subscriber) {
				Iterator<Integer> it = itr.iterator();
				// subscriber 는 반드시 시퀀셜하게 넘어온다고 가정한다.

				// Subscription 백프레셔
				subscriber.onSubscribe(new Subscription() {
					@Override
					public void request(long n) {
						es.execute(() -> {
							int i = 0;
							while (i++ < n) {
								if (it.hasNext()) {
									subscriber.onNext(it.next());
								} else {
									subscriber.onComplete();
									break;
								}
							}
						});

					}

					@Override
					public void cancel() {

					}
				});

			}
		};

		// Subscriber는 메서드가 4개다.
		Subscriber<Integer> s = new Subscriber<Integer>() {
			private Subscription subscription;

			// 백프레셔 필요한 이유
			// Publisher 가 백만개 push 하는데
			// Subscriber 가 느린경우
			// 반대의 경우도 있다.
			// 장점이 있다. 버퍼가 없기 때문에 메모리 사용률이 낮다.
			// 리액티브가 항상 일정한 크기로 흘러가게 조율이 가능하다.
			// 되게 어려운 주제다.
			@Override
			public void onSubscribe(Subscription subscription) {
				this.subscription = subscription;
				//
				System.out.println(Thread.currentThread().getName() + " onSubscribe");
				subscription.request(1); // 다 내놔. 몽땅 받고 싶다.
			}

			@Override
			public void onNext(Integer item) {
				// 옵저버의 update 같은거
				System.out.println(Thread.currentThread().getName() + " onNExt " + item);
				subscription.request(1); //

			}

			@Override
			public void onError(Throwable throwable) {
				System.out.println("onError");
			}

			@Override
			public void onComplete() {
				System.out.println("onComplete");
			}
		};

		p.subscribe(s);
		es.awaitTermination(10, TimeUnit.HOURS);
		es.shutdown();
	}
}
