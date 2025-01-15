package com.revature.globetrotters.enums;

import java.util.Arrays;
import java.util.HashSet;

public enum PublicUrl {
    CUSTOMER_LOGIN("/users/login"),
    MODERATOR_LOGIN("/moderators/login"),
    REGISTER("/users/register"),
    TRAVEL_PLANS_RECENT("/plans/recent/12");

    private final String url;
    private static final HashSet<String> urlSet = new HashSet<>();

    PublicUrl(String url) {
        this.url = url;
    }

    static {
        Arrays.stream(PublicUrl.values()).forEach(endpoint -> urlSet.add(endpoint.getUrl()));
    }

    public static boolean isPublicUrl(String url) {
        return urlSet.contains(url);
    }

    public String getUrl() {
        return url;
    }
}
