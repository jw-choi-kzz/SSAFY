https://d2.naver.com/helloworld/1329  
  
**"stop-the-world"** -> GC 실행 쓰레드 제외 나머지 모두 작업 중단 -> GC 작업 완료 -> 나머지 쓰레드 작업 재시작  
GC튜닝 = stop-the-world 시간을 줄이는 것  
  
**weak generational hypothesis**  
*(대부분 객체는 금방 unreachable 상태가 되고, 오래된 객체에서 젊은 객체로의 참조는 아주 적게 존재)*  
: Young, Old 영역  
### Young Generation 영역 : Minor GC 발생  
총 3개 영역 = **Eden 영역(새로 생성한 객체)**, Survivor 영역(2개. Eden의 GC에서 살아남은 객체가 이동됨)  
하나의 Survivor 영역이 다 차면 그 중 살아남은 객체를 다른 Survivor 영역으로 이동, 다 찼던 Survivor 영역은 다 비우는 과정 반복하고, **살아남은 객체를 Old 영역으로 이동**  
  
+) HotSpot VM의 기술 : bump-the-pointer, TLABs(Thread-Local Allocation Buffers) => 빠른 메모리 할당을 위해    
bump-the-pointer : Eden 영역에 할당된 마지막 객체 추적(top), 새로 생성된 객체가 Eden영역에 넣기 적당한 크기인지 확인  
여러 스레드에서 사용하는 객체를 Eden 영역에 저장->(Thread-Safe 위해) lock 발생, lock-contention으로 성능 매우 떨어지는 문제  
TLABs : 각 스레드가 Eden 영역의 작은 덩어리를 가지고 자기가 가진 TLAB에만 접근하게 함(lock 없이 메모리 할당)  

### Old Generation 영역 : Major GC (Full GC) 발생
Young 영역에서 살아남은 객체가 복사되는 공간, 대부분 Young 영역보다 크게 할당하고 Young 영역보다 GC 비교적 적게 발생.  
**Permanent Generation 영역** (Method Area) : 객체나 억류(intern)된 문자열 정보를 저장하는 공간 (여기서의 GC는 Major 횟수에 포함)  
  
*Old 영역에 있는 객체가 Young 영역의 객체를 참조?* => Old 영역의 card table(512바이트 chunk)에서 필요할 때 정보 표시됨  
Young 영역 GC 실행 시) Old 영역 card table만 확인하며 GC 대상인지 식별  
card table 관리는 write barrier 사용(Minor GC 시간 단축 기능, 약간의 오버헤드 발생)  
  
## GC 방식 5가지 (JDK 7 기준)
: Serial GC, Parallel GC, Parallel Old GC(Parallel Compacting GC), Concurrent Mark & Sweep GC(CMS), G1(Garbage First) GC  
+) Serial GC : 데스크톱의 CPU 코어가 하나만 있을 때 사용 ->  애플리케이션의 성능 떨어지기 때문에 운영 서버에서 절대 사용X  
* #### Serial GC ```-XX:+UseSerialGC``` : 메모리, CPU 코어 개수 적을 때 적합    
Mark-Sweep-Compact 알고리즘: Old영역 살아 있는 객체 식별(Mark)->heap 앞 부분부터 확인하며 산 것만 남김(Sweep)->각 객체가 연속되게 쌓이도록 힙의 가장 앞 부분부터 채움(Compaction)  
* #### Parallel GC (Throughput GC) ```-XX:+UseParallelGC``` : 메모리 충분, 코어 개수 많을 때 유리  
Serial GC와 알고리즘 동일, GC를 처리하는 쓰레드가 여러 개  
* #### Parallel Old GC ```-XX:+UseParallelOldGC```  
Mark-**Summary**(앞서 GC를 수행한 영역에 대해 별도로 살아 있는 객체 식별)-Compaction  
* #### CMS GC (Low Latency GC) ```-XX:+UseConcMarkSweepGC``` : 모든 애플리케이션의 응답 속도가 매우 중요할 때 유리  
Initial Mark(클래스 로더에서 가장 가까운 객체 중 산 객체만 찾음) -> Concurrent Mark(앞에서 Mark한 객체에서 참조하고 있는 객체들을 따라가면서 확인, 다른 스레드 실행 중인 상태) ->Remark(새로 추가/참조 끊긴 객체 확인)->Concurrent Sweep(쓰레기 정리. 다른 스레드 실행 중)     
(장점) stop-the-world 단축 / (단점) 메모리, CPU 더 많이 사용, Compaction 기본적으로 제공X -> Compaction 작업 수행 빈도, 시간 고려  
* #### G1(Garbage First) GC : 바둑판의 각 영역에 객체를 할당하고 GC를 실행. 성능이 장점  
    
=> 객체 크기, 생존 주기, 장비 종류 등을 고려하여 각 서비스에 가장 적합한 GC 옵션을 찾아라  
