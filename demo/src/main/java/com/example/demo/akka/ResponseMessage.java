package com.example.demo.akka;

import java.util.Map;

public record ResponseMessage(Map<String, Integer> wordCounts) {
	
}

