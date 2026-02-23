package com.example.navigator.config;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * Активация JAX-RS в контейнере.
 * Все REST-ресурсы доступны по пути /api/*,
 * при этом NavigatorController мапится на /navigator, как в OpenAPI.
 */
@ApplicationPath("/api")
public class JaxRsActivator extends Application {
}

