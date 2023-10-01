# ERD

> 상품 할인 주기, 통계 주기 모두 month로 설정
> 월 별 할인가로 판매할 상품과 할인가 지정 필요

## table

### product

- product_code(pk) : 제품 코드
- category_code(fk) : 카테고리 코드
- name: 제품의 이름
- price: 제품의 판매가 (할인 적용 X)
- stock: 제품 재고 수 (상점에 등록된 재고 수 미포함)
- img: 제품의 이미지
- barcode: 제품의 바코드 정보
- description: 제품

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

- store_product_id(pk): 매장 제품 아이디
- store_code(fk) : 매장 코드
- product_code(fk): 제품 코드
- month: 월
- incoming_cnt: 총 입고수
- sale_cnt: 판매 수량
- description: 매장 제품 설명

※ stock_cnt: 재고 수 (incoming_cnt - sale_cnt)

### discount

- discount_id(pk): 할인 아이디
- store_product_id(fk): 매장 제품 아이디
- discount_month: 월
- discount_price: 할인 판매가. 월별 할인가 데이터가 존재하지 않을 경우 product.price(제품의 판매가)로 판매

### store_statistics

- statistics_id(pk): 매장 통계 아이디
- store_code(fk): 매장 코드
- product_code(fk): 제품 코드
- month: 월
- month_revenue: 상품 월별 총 매출 (store_product.sale_cnt \* proudct.price | discount.discount_price)
- month_profit: 상품 월별 총 순이익 (revenue \* (1-store.commision_rate) - fixed_cost)
