package com.ftww.basic.kits;

public class Test {
	public void print(int n){
		if(n>1){
			int index =n-1;
			print(index);
		}
		for(int i = 1 ; i <= n ; i ++){
			System.out.print( n * i + "    ");
		}
		System.out.println();
	}
	public static void main(String[] args){
		//new Test().print(9);
		Iterator.iterate(3, new IAction());
	}
}
@SuppressWarnings({ "rawtypes", "unchecked" })
abstract class Iterator<T> {
	abstract void process(T n);
	static void iterate(int n, Iterator action) {
		for(int i = 1 ; i <= n ; i++){
			action.process(i);
		}
	}
}
class IAction extends Iterator<Integer> {
	void process(Integer n) {
		iterate(n, new JAction(n));
		System.out.println();
	}
}
class JAction extends Iterator<Integer> {
	private final int x;
	public JAction(int n){
		this.x = n;
	}
	void process(Integer n) {
		System.out.println(" " + x*n);
	}
}
