package kr.or.hamaeyu.test;

import org.junit.jupiter.api.Test;

import kr.or.hamaeyu.collector.MovieInfoCollector;

public class MovieInsertTest {
	@Test
	void main() {
        String date = "20251005";
        String numOfRows = "20";
        MovieInfoCollector.collectDailyBoxOffice(date, numOfRows);
    }
}
