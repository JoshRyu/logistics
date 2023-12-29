# ERD

## table

### product

- product_code(pk) : 제품 코드
- category_code(fk) : 카테고리 코드
- name: 제품의 이름
- price: 제품의 판매가 (할인 적용 X)
- stock: 제품 재고 수 (상점에 등록된 재고 수 미포함)
- img: 제품의 이미지
- barcode: 제품의 바코드 정보
- description: 제품 설명

### category

- category_code(pk) : 카테고리 코드
- parent_category_code(fk) : 상위 카테고리 코드
- name: 카테고리 명
- description: 카테고리 설명

### store

- store_code(pk): 매장 코드
- name: 매장명
- address: 매장 주소(오프라인 매장인 경우 존재)
- type: 매장이 매대비를 받는 매장인지 | 수수료를 가져가는 매장인지 | Both
- fixed_cost: 매대비
- comission_rate: 수수료 비율 (0~1)
- description: 매장 설명

### store_product

> 먼저 등록되어야 한다

- store_product_id(pk): 매장 제품 아이디
- store_code(fk) : 매장 코드
- product_code(fk): 제품 코드
- store_price: 특정 매장에서의 제품 판매 가격
- income_cnt: 입고 수
- stock_cnt: 재고 수
- defect_cnt: 불량/파손 수
- description: 매장 제품 설명

### sales_history

- sales_id(pk): 판매 아이디
- store_product_id(fk): 매장 제품 아이디
- sales_month: 판매 월
- quantity: 판매 수량
- sales_price: 판매 가격 (store_price - price_diff)
- memo: 메모

### store_statistics

> 매일 1회 배치로 업데이트

- statistics_id(pk): 매장 통계 아이디
- store_code(fk): 매장 코드
- month: 월
- month_revenue: 상품 월별 총 매출
- month_profit: 상품 월별 총 순이익
- last_updated_time: 마지막 업데이트 일시

```
# month_revenue =  Σ(sale_price * quantity) (store_code, sales_month로 조회)
# month_profit = (month_revenue * (1 - store.commission_rate/100)) - store.fixed_cost
```

### member

- id(pk): 사용자의 아이디
- password: 사용자의 비밀번호
- role: 사용자 권한 ADMIN | USER
- username: 사용자의 이름
