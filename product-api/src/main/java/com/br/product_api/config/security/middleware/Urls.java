package com.br.product_api.config.security.middleware;

import java.util.List;

public class Urls {

    public static final List<String> PROTECT_URLS = List.of(
            "api/product",
            "api/supplier",
            "api/category"
    );
}
