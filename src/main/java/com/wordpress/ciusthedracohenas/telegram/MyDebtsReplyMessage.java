package com.wordpress.ciusthedracohenas.telegram;

import com.wordpress.ciusthedracohenas.picpic.models.Debt;

public class MyDebtsReplyMessage extends ReplyMessage {	
	private Debt debt;	
	
	public void setDebt(Debt debt) {
		this.debt = debt;
	}
	
	@Override
	public String getText() {
		return debt.toString();
	}
}
