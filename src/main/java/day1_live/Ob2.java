package day1_live;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Ob2 {
	// 1. 옵저버는 끝이란 개념이 없다.
	//       notifyObservers 만 있다
	// 2. Exception
	//
	//  옵저버 패턴이 확장된게 리액티브의 한 축이다.



	static class IntObservable extends Observable implements Runnable {

		@Override
		public void run() {
			for (int i = 1; i <= 10 ; i++) {
				setChanged();
				notifyObservers(i);  // push
				// int i = it.next(); //  pull
			}
		}
	}

	// DATA method()
	// method(DATA)

	public static void main(String[] args) {
		// Observer
//		Observable // Source -> Event/Data -> target 인 Observer (notify)

		Observer ob = new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				System.out.println(Thread.currentThread().getName() + " " + arg);
			}
		};

		IntObservable io = new IntObservable();
		io.addObserver(ob);

		ExecutorService es = Executors.newSingleThreadExecutor();
		es.execute(io);

		// 옵저버는 비동기로 만들기 쉽다.
		System.out.println(Thread.currentThread().getName() + " EXIT");
		es.shutdown();
	}
}
