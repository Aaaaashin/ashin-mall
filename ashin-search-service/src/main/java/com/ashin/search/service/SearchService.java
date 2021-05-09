package com.ashin.search.service;

import java.io.IOException;
import java.util.Map;

public interface SearchService {

    Map search(Map<String, String> searchMap) throws IOException;
}
