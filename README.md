```java
public static void main(String[] args) throws InterruptedException {
        ExecutorService es = Executors.newFixedThreadPool(100);
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/test";

        for (int i = 0; i < 100; ++i) {
            es.execute(() -> {
                int idx = counter.getAndAdd(1);
                log.info("Thread {}", idx);

                String header = "{\"payAccountId\":" + idx + ",\"email\":\"slolee@naver.com\"}";
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.add("x-pay-account", header);
                HttpEntity<Void> entity = new HttpEntity<>(httpHeaders);

                StopWatch sw = new StopWatch();
                sw.start();

                String result = restTemplate.exchange(url + "/" + idx, HttpMethod.GET, entity, String.class).getBody();

                sw.stop();
                log.info("Elapsed {} {} {}", idx, sw.getTotalTimeSeconds(), result);
            });
        }

        es.shutdown();
        es.awaitTermination(100, TimeUnit.SECONDS);

    }
```
위와 같이 동시에 100개의 호출을 생성해 테스트했습니다.

테스트를 위해서는 외부 서버로 /delay/{time} Endpoint(아래코드 참조) 가 필요합니다.
```java
@RestController
public class TestController {
	@GetMapping("/delay/{time}")
	public String delay(@PathVariable Long time) throws InterruptedException {
		Thread.sleep(time * 1000);
		return String.format("Success Delay! (%d)", time);
	}
}
```
