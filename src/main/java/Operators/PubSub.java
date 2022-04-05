package Operators;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * 토비의 봄 TV 6회 스프링 리액티브 프로그래밍 (2) - Reactive Streams - Operators
 * <p>
 * Reactive Streams - 옵저버블 패턴을 발전시켜서 Operator 추가해보고,
 * Reactive 프로젝트 사용법도 잠깐 살펴보자
 * <p>
 * Publisher 프로바이더 역할 데이터를 계속 생산
 * <p>
 * Publisher -> Data1 -> Operator1(가공) -> [Data2] -> Operator2 -> [Data3] -> Subscriber
 *
 * 1. map(d1 -> function -> d2)
 */
@Slf4j
public class PubSub {
	public static void main(String[] args) {

		// 데이터를 보내는거
		Publisher<Integer> pub = iterPub(Stream.iterate(1, a -> a + 1).limit(10).collect(toList()));

		// 퍼블리셔 부터 4개 메서드 종류의 데이터를 받는다
		Subscriber<Integer> sub = logSub();

		pub.subscribe(sub);
	}

	private static Subscriber<Integer> logSub() {
		return new Subscriber<Integer>() {
			@Override
			public void onSubscribe(Subscription subscription) {
				log.debug("onSubscribe");
				subscription.request(Long.MAX_VALUE);
			}

			@Override
			public void onNext(Integer integer) {
				log.debug("onNext:{}", integer);
			}

			@Override
			public void onError(Throwable throwable) {
				log.debug("onError:{}", throwable);
			}

			@Override
			public void onComplete() {
				log.debug("onComplete");
			}
		};
	}

	private static Publisher<Integer> iterPub(List<Integer> iter) {
		return new Publisher<Integer>() {

			@Override
			public void subscribe(Subscriber<? super Integer> subscriber) {
				// subscriber 데이터를 받는거
				subscriber.onSubscribe(new Subscription() {
					//  Subscription 은 둘 사이에 구독이 한번 일어나는거

					@Override
					public void request(long l) {
						try {
							iter.forEach(s -> subscriber.onNext(s));

							// 반드시 완료 시그널을 줘야 한다.
							subscriber.onComplete();
						} catch (Exception e) {
							subscriber.onError(e);
						}
					}

					@Override
					public void cancel() {

					}
				});
			}
		};
	}
}
