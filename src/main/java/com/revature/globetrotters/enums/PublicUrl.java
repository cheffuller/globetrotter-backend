package com.revature.globetrotters.enums;

public enum PublicUrl {
    CUSTOMER_LOGIN("/users/login"),
    MODERATOR_LOGIN("/moderators/login"),
    REGISTER("/users/register");

    private final String url;

    PublicUrl(String url) {
        this.url = url;
    }

    // If the number of Public URL enum values increases to be > 100, switch to a hashmap-based implementation
    // for O(1) lookup time. Else, stick with O(N) for loop because loops are optimized by the compiler
    public static boolean isPublicUrl(String url) {
        for (PublicUrl publicUrl : PublicUrl.values()) {
            if (publicUrl.getUrl().equals(url)) {
                return true;
            }
        }
        return false;
    }

    public String getUrl() {
        return url;
    }
}
