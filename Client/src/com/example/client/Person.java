package com.example.client;

public abstract class Person extends Object {
	abstract void setCard(Card card, int cardNum);
	abstract Card getCard(int numCard);
}
