### 커밋으로 보는 개발핵심과정

1. **spring batch 추가 및 초기설정**
    1. 실행과정이 ddl→job→devinitData 이기때문에 
    DDL로 테이블생성→DevInitData 초기 데이터 생성→Job 로직실행
    과정을 위해서 Job 로직 내부에 [initData.run](http://initData.run) 실행
        
        ![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/ac91facb-1b30-4648-bfaf-0f1effc7f3b8/Untitled.png)
        
    2. 뒤에 발생할 devinitData 를 무시하기위해 내부에서 중복수행체크
    
    ![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/b5fdea4b-665f-4d8f-b36b-7efd11968baf/Untitled.png)
    
2. **정산페이지 에서 정산데이터 생성**
    1. 정산데이터를 수동으로 생성하는 과정은 스프링배치를 이용해서 정산데이터를 생성하는 과정과 플로우가 똑같다.
        1. 정산전체과정
            1. 주문 품목 복사해서 정산 테이블 만들기
            2. 정산에  필요데이터 한테이블에모으기(주문,회원 정보 등등)
            3. 정산에 필요한 makedata 쿼리작성
            4. 쿼리에 맞게 itemReader 작성
            5. 쿼리에 맞게 itemProcessor 작성
            6. 쿼리에 맞게 itemWriter 작성
    2. 4~6번과정은 날짜범위네 데이터 추출→ 주문상품 데이터를 이용해서 정산에 필요한 데이터로 변경후 저장→정산데이터 저장 의 과정을 가진다.
    3. 정산데이터로 변경시 fk 설정은 제거하고 join 만 가능하도록해야 원본 테이블들의 작업을 방해하지 않는다.foreignKey설정은 정확하게는 테이블 생성시에만 사용된다.
        
        ![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/b98f698d-dfc6-47bf-960d-8aee121c5c60/Untitled.png)
        
    4. 연월 기입시 지난달 15일~이번달 15일 까지의 정산데이터를 만들수있도록 로직설정했다.
    정산주문품목을 구분하는 기준은 orderItem 이기때문에 중복시 삭제후 생성하였지만 이후 개발 과정에서 update  방식으로 정산이 중복적으로 반복되지 않도록 정산내용을 가져가도록 수정했다.
        
        ![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/c1ce268a-fd7f-4cfe-9f5f-667629685ae9/Untitled.png)
        
        **로직 수정후**
        
        ![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/3b7000ca-bbc1-477e-9a6e-21d7fa45a02b/Untitled.png)
        
        ![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/361be909-919b-4cf0-ac66-c6b5c876d705/Untitled.png)
        
3. **건별정산**
    1. 정산은 예치금 로그를 남기고 정산 금액만큼 판매자에게 예치금을 충전시켜주고
    정산 상품 속성인 RebateOrderItem.rebateDate, RebateOrderItem.rebateCashLog 값을 업데이트 해주는 과정글 가진다.
    
    ![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/a68a478c-a123-4a73-8a5e-67da8114daea/Untitled.png)
    
    ![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/8bd412ef-9997-452c-abaa-de9d2bec1558/Untitled.png)
    
4. **정산금액 로직 변경,spring batch 를 이용해 15일 새벽4시에 정산데이터 생성하도록 설정**
    1. batch 설정시에 주의해야하는것은 Page 형식으로 값을 return 하도록 itemReader 에 사용되는 쿼리를 새로 생성해주어야한다.
    2. 그밖에 다른 로직들은 batch 생성방식에 맞춰서 이전에 작성한 건별생성 로직을 이용하여 작성하였다.
    3. 이후 scheduling을 위해서 JobScheculer 클래스를 이용해서 키값이 yearMonth 인  jobparameter 값으로 해당 연월을 제공하여서 구현하였다.
        
        ![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/eb737752-89f3-4352-b6e0-1ba33f28bfb9/Untitled.png)
