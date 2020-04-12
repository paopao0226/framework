package org.csu.mypetstore.domain;

public class AlipayConfig {

    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号,开发时使用沙箱提供的APPID，生产环境改成自己的APPID
    public static String APP_ID = "2016102300747569";


    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String APP_PRIVATE_KEY = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCjVhD85P2tPkTelWiEv6lEbwrItkDf8mQX9bQDxuiNPW5/EEFZ1LfouMzQmtUlZULOzSjEfqRTGwZ+CYigG3M7ankdgOP4MXp3tKz3pLyNpAEcw0vZ6RNEkTSBEB/GXy6kXTxE1nI/XKXnvC1VWONFvuuCJ1DY2kLc01zUlrdoc7m3WWSHwoZ3wznzaHB3tJaNn4z0Ig15C442oL5xDSkKuP0FmuTVAbAf5IAXWKpm77GaeuACRayl1gN3GcNegD4trXgJ6QDAtSu/pG6aiWxnklRaqUeP8IL7mtpXEIE8v/WNPBFkrHWxqFn+oFh272TDaayNTQ/c+CpbOlaFOOj9AgMBAAECggEAQW6jE4xwi0xcroZP/o4BMycAvPM1/gDOVELbwH+YfaxUbZaHd4MdwZ72fcLpDDgKkWnioQTSs3AqI6eSEgGtIeo/ZrBRowW/N+ErQXG/D0baIzmhU0RpsZgx3lolQIS03Kd1E2aNEhD9bV4XEw+uzCv1kIhLfPPCwU/y6j8Obkr5yTkObG06bHqUlTV9cD8hPMTChnm86R10wvjAlNaBeUkYiZDFe6XRclXTpAp2qUEnS66p9PUijacZ7Zqfu+/skqCXzi2mD5zDz25o/83QglGVYPaV9h26LOoyRcjWm+LFzZkrcfRt5PiadCuDE08qqhWsX8RGLvsENK+hJ3+VQQKBgQDYp4h1yY/M4RwOXTNR4GPNI6dfeHJCrgIDPFHFDnfMwGg2SSiBi7UciotcYTVTwBaFExC1rU1nc9d1MgPdQ107jgtbDC6rTaOfWyJwERCtZhfRoaxFKFMZ4cPRiHACpeNpCEkiGowOMYlPMJzOd/rug35+r6PrDvFT0/uqHAirGQKBgQDA/7gNP7GEbfFnOau5GzhbJt0itMQPfVasdUqhLrezOzcxEqvFeUfEtPe6cS2+3z/TdMV57CMU3Ldl1fV+SuFmuIuQoRSG+MPH6QA0pwJKCGGcG83iUUTUoKb4bQJ3t7VkcIVnCgGD0qrgKpHU2vPHlFawJK4PB0+vZB+qzP3NhQKBgDurmLfoNcTV0yZZoQLR7GrZgQWYZM/coQ1HFbZST8iCzbD14UQcuFsOBWgCFe/lHc60+4suGf1+OD888TeFkC4Q5yvCZTAlCAWh0hBPgupfDfxcrNe3TXv5ZDdXfpIcIlARSiMscJqQJiM7XlS3UtNSQAqgUWMyEGFoJw4o/nIxAoGAacP5zjDojIVftM0TAXccVD+hEiL0CvUKKaqBgoFCpJM/9MGhpq+HGBKAbAkYI7DenngwvFcfBpDKZKFzTomkqpFZ0qGjyS/MwuI6faBEjeO38j/+fRdCXC/KMPw0TP09B9TuFoCSQfaSNBTAJGD1FZuCQjdnt86u6ZQ6EnUSZa0CgYB5GOOXWvBJc6dqtm5u2keDttqNCwWJXAb6JC9s6el/gIYSO7gM14ED4eJ64iyAxEp70zwtBE8nfvTnp2i9iYL1jBIKzeofW5qQOFx05khiSsYQyUJoD2eAWxepc0r8OaufoOUTSTFT91bokUcCIr+zuXffxeBSBQ65FJU8fjXKBg==";

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmydZhS7CtlUmsJauWV3NBw8c1hOTEDqhLqM/RmwpXgYO6YfXfl7Bur5lszM5fdT3WKS0kJAu5SjKPte5Nw9YH64tXc7y5XZ3DTLMMMGq/L7GnYeVBl5bl267cR8XTxs3y0kczzIAyaGxecVaMrVDeHUQAS3ltVKgfTQLJ6U4zoTxmYPZ5xqQQJnyMO8LlBqpY6jD9VZ+SXOzU4jWLk1+4KBmmxcxvkX7ERxlUFwqDgCk8hzTWvnptmQi8oXu/pvMy6sMzAvLPGy7OKxNrZjMjXDnNbyNBzyFmYCkJIxwUKSSWkMxsqtdB+JRnzpf7lR+OYt/1ddfElj0c8cKoWXO+QIDAQAB";

    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://localhost:8080/alipay/notify_url";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问(其实就是支付成功后返回的页面)
    public static String return_url = "http://localhost:8080//alipay/return_url";

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String CHARSET = "utf-8";

    // 支付宝网关，这是沙箱的网关
    public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

}
