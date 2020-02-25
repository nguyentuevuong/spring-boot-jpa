INSERT INTO [app_user](PASSWORD, USERNAME, DISPLAY_NAME) VALUES('$2a$10$bnC26zz//2cavYoSCrlHdecWF8tkGfPodlHcYwlACBBwJvcEf0p2G', 'nguyentuevuong@gmail.com', 'Vuong NV')

INSERT INTO [user_role](APP_USER_ID, ROLE) values(1, 'ADMIN')
INSERT INTO [user_role](APP_USER_ID, ROLE) values(1, 'PREMIUM_MEMBER')