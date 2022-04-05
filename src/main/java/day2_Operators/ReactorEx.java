package day2_Operators;

import reactor.core.publisher.Flux;

public class ReactorEx {
	public static void main(String[] args) {
		// 일종의 퍼블리셔라고 보면 됨.
		Flux.<Integer>create(s -> {
					s.next(1);
					s.next(2);
					s.next(3);
					s.complete();

				})
				.log() // 동작이 궁금하면 log() 사용하라.
				.map(s -> s * 10)
				.log() // 동작이 궁금하면 log() 사용하라.
				.reduce(0, (a,b)-> a+b)
				.log() // 동작이 궁금하면 log() 사용하라.
				.subscribe(s -> System.out.println(s)); // consumer 가 onNext
	}
}
