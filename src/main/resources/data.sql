INSERT INTO [app_user]([username], [display_name], [password]) VALUES('nguyentuevuong@gmail.com', 'Vuong NV', '$2a$10$bnC26zz//2cavYoSCrlHdecWF8tkGfPodlHcYwlACBBwJvcEf0p2G')

INSERT INTO [user_role]([app_user_id], [role]) values(1, 'ADMIN')
INSERT INTO [user_role]([app_user_id], [role]) values(1, 'PREMIUM_MEMBER')