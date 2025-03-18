
# 🛍️🛒 SellOn (Sell + Online)
SellOn은 스마트 온라인 스토어 플랫폼입니다.
판매자는 간편하게 스토어를 개설하고 상품을 등록할 수 있으며, 구매자는 다양한 상품을 한 곳에서 손쉽게 구매할 수 있습니다.
실시간 인기 상품 추천, 편리한 결제 및 정산 시스템, 물류 연동까지 지원하여 효과적인 온라인 거래 환경을 제공합니다.

### ✨ 주요 기능
- 구매자와 판매자 간의 거래 지원
    - 제품 등록 및 판매
    - 배송
- 베스트 셀러 추천 시스템
    - 품목별 판매량 기반
    - 유저 행동 기반 맞춤 추천
 
# 🎯 프로젝트 목표
- **확장성과 안정성을 고려한 아키텍처 설계**
   - 단순한 기능 구현을 넘어 **대용량 트래픽 처리**를 고려한 설계
   - **MSA(Microservices Architecture)** 기반의 서비스 분리 및 운영
- **객체지향과 스프링의 핵심 원칙 적용**
   - 유지보수성과 확장성을 고려한 **객체지향적 코드** 작성
   - **IoC/DI, AOP 등의 스프링 개념**을 적극 활용하여 효율적 애플리케이션 개발
- **최신 기술 스택 활용 및 기술적 도전**
   - **Redis, Kafka, RabbitMQ** 등을 활용한 데이터 처리 및 메시지 큐 활용
   - **도커, 쿠버네티스** 기반의 컨테이너화 및 배포 자동화 (CI/CD 구축)
- **프로덕트 품질과 안정성 보장**
   - **단계별 테스트(단위테스트, 성능테스트)** 를 적용하여 신뢰성 높은 애플리케이션 구축
   - **로깅 및 모니터링**을 통한 실시간 상태 파악 및 장애 대응
- **명확하고 체계적인 문서화**

# 📐 설계
- 커뮤니케이션 다이어그램
- 클래스 다이어그램
- ER 다이어그램

# 🔀 Git 브랜치 전략
![](https://velog.velcdn.com/images/bienlee/post/e395d39b-c950-4de8-896d-b7edc8242bee/image.png)
1. **master(main)**: 항상 제품화가 가능한 상태를 유지하며, 배포 이력을 담고 있습니다.
2. **develop**: 다음 릴리스를 위한 개발 변경사항이 통합되는 브랜치입니다.
3. **feature**: develop에서 분기하여 새로운 기능을 개발하고 완료 후 develop으로 병합됩니다.
4. **release**: develop에서 분기하여 릴리스를 위한 버그 수정과 마무리 작업을 수행합니다.
5. **hotfix**: master에서 분기하여 긴급한 버그를 수정하고 master와 develop 모두에 병합됩니다.

### 📋 PR 규칙
- feature 브랜치에서 작업한 코드는 develop 브랜치로 PR을 요청합니다.
- PR은 최소 1명 이상의 승인(approve)을 받은 후에만 병합할 수 있습니다.
- 코드 리뷰는 한 번에 최대 5개 파일로 제한합니다.

#  내용 정리
- 자바의 금액계산: [[Java] 금액 계산 타입: BigDecima](https://velog.io/@bienlee/Java-%EA%B8%88%EC%95%A1-%EA%B3%84%EC%82%B0-%ED%83%80%EC%9E%85-BigDecimal)
- 외부 Mapper 클래스 사용 :[[Spring] DTO와 MapStructure: 효율적인 객체 매핑 구현하기](https://velog.io/@bienlee/%EC%99%B8%EB%B6%80-Mapping-Class-MapStructure)
