INSERT INTO CATEGORY (category_code, description, name, parent_category_code)
VALUES ((SELECT nextval('public.category_code_seq')), '겨울 한정 상품', '목도리', null);

INSERT INTO CATEGORY (category_code, description, name, parent_category_code)
VALUES ((SELECT nextval('public.category_code_seq')), '겨울 한정 상품', '귀도리', null);