package controller;

public class Pair <S, T> {

	S s;
	T t;
	
	public Pair (S s, T t) {
		this.s = s;
		this.t = t;
	}
	
	public S getLeft () {
		return s;
	}
	
	public T getRight () {
		return t;
	}
	
	public void setLeft (S s) {
		this.s = s;
	}
	
	public void setRight (T t) {
		this.t = t;
	}
}
