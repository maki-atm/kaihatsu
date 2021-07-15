package com.example.demo;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class Difference {

	private long dif;
	private boolean bl;

	public Difference(long dif, boolean bl) {
		this.dif = dif;
		this.bl = bl;
	}

	public Difference() {

	}

	public long getDif() {
		return dif;
	}

	public void setDif(long dif) {
		this.dif = dif;
	}

	public boolean getBl() {
		return bl;
	}

	public void setBl(boolean bl) {
		this.bl = bl;
	}

	//残り日数を格納する配列を返すメソッド
	@SuppressWarnings("null")
	public ArrayList<Difference> getDifDay(List<Task> t) {

		Difference d = null;

		//今日の日付取得
		long miliseconds = System.currentTimeMillis();
		Date today = new Date(miliseconds);

		ArrayList<Difference> list =  new ArrayList<>();

		for (Task i : t) {
			LocalDate localD = i.getDate().toLocalDate();
			LocalDate localT = today.toLocalDate();
			dif = (ChronoUnit.DAYS.between(localT, localD));
			if (dif >= 0) {
				bl = true;
				d = new Difference(dif, bl);
				list.add(d);
			} else {
				dif=Math.abs(dif);
				bl = false;
				d = new Difference(dif, bl);
				list.add(d);
			}

		}
		return list;
	}
}