## 📖 Key Changes(write include commit)
* feat #7 :장바구니에 도서 추가 기능 구현 a8830bc
  * 이미 장바구니에 담았다면 개수만 증가
* feat #7 :장바구니의 상품 주문 구현 7bb86f3
  * 장바구니에서 주문 생성의 순서는 order 를 생성하고 orderItem 생성 및 연관관계 추가
* feat #7 :회원에 예치금 도입,예치금 로그 도입 cede2a3
  * cashLog 의 enum 타입 EventType 을 converter 이용해서 DB와 스프링내부에서 값의 차이를 둠
* feat #7 :예치금으로 결제 기능 구현 967841a
  * MemberService.addCash 매개변수에 EventType 추가
  * 주문상품의 전체 결제금액을 주문에 payPrice 로 저장
  * 주문에 주문상품정보를 가져올수있는 orderItems 추가
  * 결제에 대한 정보는 주문에서 수정및 변경(나중에 분리 필요)
* feat #7 :주문 생성,주문상세,주문리스트 Ui 및 controller 작업 1f48672
  * OrderMapper 내부에 OrderItemDto 가 추가로 들어가 mapper 사용하기 복잡해서 직접만듬


## 👀 To Reviewers
* 서비스가 점점 커질수록 DB구조가 헷갈린는데 작업할때 팁이 있나요?
* 주문 리스트는 모든 주문을 보이게하는게 좋을까요? 아니면 결제 안된 주문만 보는게 좋을까요?
    **참고: [Refactoring]**
    
    - Refactoring 시 주로 다루어야 할 이슈들에 대해 리스팅합니다.
    - 1차 리팩토링은 기능 개발을 종료한 후, 스스로 코드를 다시 천천히 읽어보면서 진행합니다.
    - 2차 리팩토링은 피어리뷰를 통해 전달받은 다양한 의견과 피드백을 조율하여 진행합니다.
