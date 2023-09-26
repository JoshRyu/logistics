# ERD

> 상품 할인 주기, 통계 주기 모두 month로 설정
> 월 별 할인가로 판매할 상품과 할인가 지정 필요

## product

## store

- type: 매장이 매대비를 받는 매장인지 | 수수료를 가져가는 매장인지 | Both
- fixed_cost: 매대비
- comission_rate: 수수료 비율 (0~1)

## category

## store_product

- incoming_cnt: 총 입고수
- sale_cnt: 판매 수량
- stock_cnt: 재고 수 (incoming_cnt - sale_cnt)

## discount

- discount_price: 할인 판매가. 월별 할인가 데이터가 존재하지 않을 경우 제품의 기본 price로 판매

## store_statistics

: 특정 매장 통계

- revenue: 상품 월별 총 매출 (store_product.sale_cnt \* proudct.price | discount.discount_price)
- profit: 상품 월별 총 순이익 (revenue \* (1-store.commision_rate) - fixed_cost)
