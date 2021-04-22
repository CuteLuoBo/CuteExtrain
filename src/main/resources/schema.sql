/*
 Navicat Premium Data Transfer

 Source Server         : database
 Source Server Type    : SQLite
 Source Server Version : 3021000
 Source Schema         : main

 Target Server Type    : SQLite
 Target Server Version : 3021000
 File Encoding         : 65001

 Date: 22/04/2021 17:10:55
*/

PRAGMA foreign_keys = false;

-- ----------------------------
-- Table structure for command_limit
-- ----------------------------
DROP TABLE IF EXISTS "command_limit";
CREATE TABLE "command_limit" (
  "id" integer NOT NULL PRIMARY KEY AUTOINCREMENT,
  "group_id" INTEGER NOT NULL DEFAULT 0,
  "user_id" INTEGER NOT NULL DEFAULT 0,
  "primary" TEXT NOT NULL,
  "cycle_num" integer NOT NULL,
  "cycle_second" integer NOT NULL,
  "state" integer NOT NULL DEFAULT 0
);

-- ----------------------------
-- Records of command_limit
-- ----------------------------
INSERT INTO "command_limit" VALUES (1, 0, 0, 'yr', 2, 60, 0);

-- ----------------------------
-- Table structure for sqlite_sequence
-- ----------------------------
DROP TABLE IF EXISTS "sqlite_sequence";
CREATE TABLE "sqlite_sequence" (
  "name",
  "seq"
);

-- ----------------------------
-- Records of sqlite_sequence
-- ----------------------------
INSERT INTO "sqlite_sequence" VALUES ('unit_yys', 179);
INSERT INTO "sqlite_sequence" VALUES ('command_limit', 1);

-- ----------------------------
-- Table structure for unit_yys
-- ----------------------------
DROP TABLE IF EXISTS "unit_yys";
CREATE TABLE "unit_yys" (
  "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
  "unit_id" INTEGER NOT NULL,
  "level" TEXT,
  "name" TEXT NOT NULL,
  "special_name" TEXT,
  "can_roll" integer NOT NULL DEFAULT TRUE
);

-- ----------------------------
-- Records of unit_yys
-- ----------------------------
INSERT INTO "unit_yys" VALUES (0, 200, 'SR', '桃花妖', NULL, 1);
INSERT INTO "unit_yys" VALUES (1, 201, 'SR', '雪女', NULL, 1);
INSERT INTO "unit_yys" VALUES (2, 202, 'N', '三尾狐', NULL, 1);
INSERT INTO "unit_yys" VALUES (3, 203, 'N', '灯笼鬼', NULL, 1);
INSERT INTO "unit_yys" VALUES (4, 205, 'R', '座敷童子', NULL, 1);
INSERT INTO "unit_yys" VALUES (5, 206, 'R', '鲤鱼精', NULL, 1);
INSERT INTO "unit_yys" VALUES (6, 207, 'N', '九命猫', NULL, 1);
INSERT INTO "unit_yys" VALUES (7, 208, 'R', '狸猫', NULL, 1);
INSERT INTO "unit_yys" VALUES (8, 209, 'R', '河童', NULL, 1);
INSERT INTO "unit_yys" VALUES (9, 210, 'SR', '鬼使白', NULL, 1);
INSERT INTO "unit_yys" VALUES (10, 211, 'SR', '鬼使黑', NULL, 1);
INSERT INTO "unit_yys" VALUES (11, 212, 'R', '童男', NULL, 1);
INSERT INTO "unit_yys" VALUES (12, 213, 'R', '童女', NULL, 1);
INSERT INTO "unit_yys" VALUES (13, 214, 'N', '饿鬼', NULL, 1);
INSERT INTO "unit_yys" VALUES (14, 215, 'SR', '孟婆', NULL, 1);
INSERT INTO "unit_yys" VALUES (15, 216, 'R', '巫蛊师', NULL, 1);
INSERT INTO "unit_yys" VALUES (16, 217, 'SSR', '大天狗', NULL, 1);
INSERT INTO "unit_yys" VALUES (17, 218, 'SR', '鸦天狗', NULL, 1);
INSERT INTO "unit_yys" VALUES (18, 219, 'SSR', '酒吞童子', NULL, 1);
INSERT INTO "unit_yys" VALUES (19, 220, 'SR', '犬神', NULL, 1);
INSERT INTO "unit_yys" VALUES (20, 221, 'R', '食发鬼', NULL, 1);
INSERT INTO "unit_yys" VALUES (21, 222, 'R', '武士之灵', NULL, 1);
INSERT INTO "unit_yys" VALUES (22, 223, 'SR', '骨女', NULL, 1);
INSERT INTO "unit_yys" VALUES (23, 224, 'R', '雨女', NULL, 1);
INSERT INTO "unit_yys" VALUES (24, 225, 'R', '跳跳弟弟', NULL, 1);
INSERT INTO "unit_yys" VALUES (25, 226, 'R', '跳跳妹妹', NULL, 1);
INSERT INTO "unit_yys" VALUES (26, 227, 'R', '兵俑', NULL, 1);
INSERT INTO "unit_yys" VALUES (27, 228, 'R', '丑时之女', NULL, 1);
INSERT INTO "unit_yys" VALUES (28, 230, 'R', '独眼小僧', NULL, 1);
INSERT INTO "unit_yys" VALUES (29, 231, 'SR', '鬼女红叶', NULL, 1);
INSERT INTO "unit_yys" VALUES (30, 232, 'R', '铁鼠', NULL, 1);
INSERT INTO "unit_yys" VALUES (31, 233, 'SR', '跳跳哥哥', NULL, 1);
INSERT INTO "unit_yys" VALUES (32, 234, 'R', '椒图', NULL, 1);
INSERT INTO "unit_yys" VALUES (33, 236, 'R', '管狐', NULL, 1);
INSERT INTO "unit_yys" VALUES (34, 237, 'R', '山兔', NULL, 1);
INSERT INTO "unit_yys" VALUES (35, 238, 'R', '萤草', NULL, 1);
INSERT INTO "unit_yys" VALUES (36, 241, 'R', '蝴蝶精', NULL, 1);
INSERT INTO "unit_yys" VALUES (37, 242, 'SR', '傀儡师', NULL, 1);
INSERT INTO "unit_yys" VALUES (38, 243, 'R', '山童', NULL, 1);
INSERT INTO "unit_yys" VALUES (39, 244, 'R', '首无', NULL, 1);
INSERT INTO "unit_yys" VALUES (40, 245, 'N', '提灯小僧', NULL, 1);
INSERT INTO "unit_yys" VALUES (41, 246, 'N', '赤舌', NULL, 1);
INSERT INTO "unit_yys" VALUES (42, 247, 'SR', '海坊主', NULL, 1);
INSERT INTO "unit_yys" VALUES (43, 248, 'SSR', '荒川之主', NULL, 1);
INSERT INTO "unit_yys" VALUES (44, 249, 'R', '觉', NULL, 1);
INSERT INTO "unit_yys" VALUES (45, 250, 'R', '青蛙瓷器', NULL, 1);
INSERT INTO "unit_yys" VALUES (46, 251, 'SR', '判官', NULL, 1);
INSERT INTO "unit_yys" VALUES (47, 252, 'SR', '凤凰火', NULL, 1);
INSERT INTO "unit_yys" VALUES (48, 253, 'SR', '吸血姬', NULL, 1);
INSERT INTO "unit_yys" VALUES (49, 254, 'SR', '妖狐', NULL, 1);
INSERT INTO "unit_yys" VALUES (50, 255, 'SSR', '阎魔', NULL, 1);
INSERT INTO "unit_yys" VALUES (51, 256, 'SR', '妖琴师', NULL, 1);
INSERT INTO "unit_yys" VALUES (52, 257, 'SR', '食梦貘', NULL, 1);
INSERT INTO "unit_yys" VALUES (53, 258, 'SSR', '两面佛', NULL, 0);
INSERT INTO "unit_yys" VALUES (54, 259, 'SSR', '小鹿男', NULL, 1);
INSERT INTO "unit_yys" VALUES (55, 260, 'SR', '清姬', NULL, 1);
INSERT INTO "unit_yys" VALUES (56, 261, 'SR', '镰鼬', NULL, 1);
INSERT INTO "unit_yys" VALUES (57, 262, 'SR', '姑获鸟', NULL, 1);
INSERT INTO "unit_yys" VALUES (58, 263, 'SR', '二口女', NULL, 1);
INSERT INTO "unit_yys" VALUES (59, 264, 'SR', '白狼', NULL, 1);
INSERT INTO "unit_yys" VALUES (60, 265, 'SSR', '茨木童子', NULL, 1);
INSERT INTO "unit_yys" VALUES (61, 266, 'SSR', '青行灯', NULL, 1);
INSERT INTO "unit_yys" VALUES (62, 267, 'SR', '樱花妖', NULL, 1);
INSERT INTO "unit_yys" VALUES (63, 268, 'SR', '惠比寿', NULL, 1);
INSERT INTO "unit_yys" VALUES (64, 269, 'SSR', '妖刀姬', NULL, 1);
INSERT INTO "unit_yys" VALUES (65, 270, 'SR', '络新妇', NULL, 1);
INSERT INTO "unit_yys" VALUES (66, 271, 'SR', '般若', NULL, 1);
INSERT INTO "unit_yys" VALUES (67, 272, 'SSR', '一目连', NULL, 1);
INSERT INTO "unit_yys" VALUES (68, 273, 'SR', '青坊主', NULL, 1);
INSERT INTO "unit_yys" VALUES (69, 274, 'R', '古笼火', NULL, 1);
INSERT INTO "unit_yys" VALUES (70, 275, 'SR', '万年竹', NULL, 0);
INSERT INTO "unit_yys" VALUES (71, 276, 'SR', '夜叉', NULL, 1);
INSERT INTO "unit_yys" VALUES (72, 277, 'SR', '黑童子', NULL, 1);
INSERT INTO "unit_yys" VALUES (73, 278, 'SR', '白童子', NULL, 1);
INSERT INTO "unit_yys" VALUES (74, 279, 'SSR', '花鸟卷', NULL, 1);
INSERT INTO "unit_yys" VALUES (75, 280, 'SSR', '辉夜姬', NULL, 1);
INSERT INTO "unit_yys" VALUES (76, 281, 'SR', '烟烟罗', NULL, 1);
INSERT INTO "unit_yys" VALUES (77, 282, 'SR', '金鱼姬', NULL, 1);
INSERT INTO "unit_yys" VALUES (78, 283, 'SSR', '荒', NULL, 1);
INSERT INTO "unit_yys" VALUES (79, 285, 'SR', '鸩', NULL, 1);
INSERT INTO "unit_yys" VALUES (80, 286, 'SR', '以津真天', NULL, 1);
INSERT INTO "unit_yys" VALUES (81, 287, 'SR', '匣中少女', NULL, 1);
INSERT INTO "unit_yys" VALUES (82, 288, 'SSR', '彼岸花', NULL, 1);
INSERT INTO "unit_yys" VALUES (83, 289, 'R', '兔丸', NULL, 1);
INSERT INTO "unit_yys" VALUES (84, 290, 'SR', '小松丸', NULL, 1);
INSERT INTO "unit_yys" VALUES (85, 291, 'SR', '书翁', NULL, 1);
INSERT INTO "unit_yys" VALUES (86, 292, 'SSR', '雪童子', NULL, 1);
INSERT INTO "unit_yys" VALUES (87, 293, 'SR', '百目鬼', NULL, 1);
INSERT INTO "unit_yys" VALUES (88, 294, 'SSR', '奴良陆生', NULL, 0);
INSERT INTO "unit_yys" VALUES (89, 295, 'SR', '追月神', NULL, 1);
INSERT INTO "unit_yys" VALUES (90, 296, 'SSR', '山风', NULL, 1);
INSERT INTO "unit_yys" VALUES (91, 297, 'SR', '日和坊', NULL, 0);
INSERT INTO "unit_yys" VALUES (92, 298, 'SR', '薰', NULL, 1);
INSERT INTO "unit_yys" VALUES (93, 300, 'SSR', '玉藻前', NULL, 1);
INSERT INTO "unit_yys" VALUES (94, 301, 'R', '数珠', NULL, 1);
INSERT INTO "unit_yys" VALUES (95, 302, 'R', '小袖之手', NULL, 0);
INSERT INTO "unit_yys" VALUES (96, 303, 'SR', '弈', NULL, 1);
INSERT INTO "unit_yys" VALUES (97, 304, 'SSR', '御馔津', NULL, 1);
INSERT INTO "unit_yys" VALUES (98, 305, 'SSR', '卖药郎', NULL, 0);
INSERT INTO "unit_yys" VALUES (99, 306, 'R', '虫师', NULL, 1);
INSERT INTO "unit_yys" VALUES (100, 307, 'SR', '猫掌柜', NULL, 1);
INSERT INTO "unit_yys" VALUES (101, 308, 'SSR', '鬼灯', NULL, 0);
INSERT INTO "unit_yys" VALUES (102, 309, 'SR', '阿香', NULL, 0);
INSERT INTO "unit_yys" VALUES (103, 310, 'R', '蜜桃&芥子', NULL, 0);
INSERT INTO "unit_yys" VALUES (104, 311, 'SSR', '面灵气', NULL, 1);
INSERT INTO "unit_yys" VALUES (105, 312, 'SSR', '鬼切', NULL, 1);
INSERT INTO "unit_yys" VALUES (106, 313, 'SSR', '犬夜叉', NULL, 0);
INSERT INTO "unit_yys" VALUES (107, 314, 'SSR', '杀生丸', NULL, 0);
INSERT INTO "unit_yys" VALUES (108, 315, 'SP', '少羽大天狗', NULL, 1);
INSERT INTO "unit_yys" VALUES (109, 316, 'SSR', '白藏主', NULL, 1);
INSERT INTO "unit_yys" VALUES (110, 317, 'SR', '人面树', NULL, 1);
INSERT INTO "unit_yys" VALUES (111, 318, 'SR', '於菊虫', NULL, 1);
INSERT INTO "unit_yys" VALUES (112, 319, 'SSR', '桔梗', NULL, 0);
INSERT INTO "unit_yys" VALUES (113, 320, 'SR', '一反木绵', NULL, 1);
INSERT INTO "unit_yys" VALUES (114, 321, 'SR', '入殓师', NULL, 1);
INSERT INTO "unit_yys" VALUES (115, 322, 'SP', '炼狱茨木童子', NULL, 1);
INSERT INTO "unit_yys" VALUES (116, 323, 'R', '天井下', NULL, 1);
INSERT INTO "unit_yys" VALUES (117, 324, 'SR', '化鲸', NULL, 1);
INSERT INTO "unit_yys" VALUES (118, 325, 'SSR', '八岐大蛇', NULL, 1);
INSERT INTO "unit_yys" VALUES (119, 326, 'SP', '稻荷神御馔津', NULL, 1);
INSERT INTO "unit_yys" VALUES (120, 327, 'SP', '苍风一目连', NULL, 1);
INSERT INTO "unit_yys" VALUES (121, 328, 'SP', '赤影妖刀姬', NULL, 1);
INSERT INTO "unit_yys" VALUES (122, 329, 'SR', '海忍', NULL, 1);
INSERT INTO "unit_yys" VALUES (123, 330, 'SSR', '不知火', NULL, 1);
INSERT INTO "unit_yys" VALUES (124, 331, 'SP', '御怨般若', NULL, 1);
INSERT INTO "unit_yys" VALUES (125, 332, 'SR', '久次良', NULL, 1);
INSERT INTO "unit_yys" VALUES (126, 333, 'SSR', '大岳丸', NULL, 1);
INSERT INTO "unit_yys" VALUES (127, 334, 'SP', '骁浪荒川之主', NULL, 1);
INSERT INTO "unit_yys" VALUES (128, 335, 'SR', '蟹姬', NULL, 1);
INSERT INTO "unit_yys" VALUES (129, 336, 'SR', '朽木露琪亚', NULL, 0);
INSERT INTO "unit_yys" VALUES (130, 337, 'SSR', '黑崎一护', NULL, 0);
INSERT INTO "unit_yys" VALUES (131, 338, 'SSR', '泷夜叉姬', NULL, 1);
INSERT INTO "unit_yys" VALUES (132, 339, 'SP', '烬天玉藻前', NULL, 1);
INSERT INTO "unit_yys" VALUES (133, 340, 'SR', '纸舞', NULL, 1);
INSERT INTO "unit_yys" VALUES (134, 341, 'SP', '鬼王酒吞童子', NULL, 1);
INSERT INTO "unit_yys" VALUES (135, 342, 'SR', '星熊童子', NULL, 1);
INSERT INTO "unit_yys" VALUES (136, 343, 'SP', '天剑韧心鬼切', NULL, 1);
INSERT INTO "unit_yys" VALUES (137, 344, 'SSR', '云外镜', NULL, 1);
INSERT INTO "unit_yys" VALUES (138, 345, 'SSR', '鬼童丸', NULL, 1);
INSERT INTO "unit_yys" VALUES (139, 346, 'SP', '聆海金鱼姬', NULL, 1);
INSERT INTO "unit_yys" VALUES (140, 347, 'SSR', '缘结神', NULL, 1);
INSERT INTO "unit_yys" VALUES (141, 348, 'SP', '浮世青行灯', NULL, 1);
INSERT INTO "unit_yys" VALUES (142, 349, 'SR', '风狸', NULL, 1);
INSERT INTO "unit_yys" VALUES (143, 350, 'SR', '蝎女', NULL, 1);
INSERT INTO "unit_yys" VALUES (144, 351, 'SSR', '铃鹿御前', NULL, 1);
INSERT INTO "unit_yys" VALUES (145, 352, 'SP', '缚骨清姬', NULL, 1);
INSERT INTO "unit_yys" VALUES (146, 353, 'SSR', '紧那罗', NULL, 1);
INSERT INTO "unit_yys" VALUES (147, 354, 'SP', '待宵姑获鸟', NULL, 1);
INSERT INTO "unit_yys" VALUES (148, 355, 'SP', '麓铭大岳丸', NULL, 1);
INSERT INTO "unit_yys" VALUES (149, 356, 'SSR', '千姬', NULL, 1);
INSERT INTO "unit_yys" VALUES (150, 357, 'SP', '初翎山风', NULL, 1);
INSERT INTO "unit_yys" VALUES (151, 358, 'SP', '夜溟彼岸花', NULL, 1);
INSERT INTO "unit_yys" VALUES (152, 359, 'SSR', '灶门炭治郎', NULL, 0);
INSERT INTO "unit_yys" VALUES (153, 360, 'SSR', '灶门祢豆子', NULL, 0);
INSERT INTO "unit_yys" VALUES (154, 361, 'N', '垢尝', NULL, 1);
INSERT INTO "unit_yys" VALUES (155, 362, 'SP', '蝉冰雪女', NULL, 1);
INSERT INTO "unit_yys" VALUES (156, 400, 'N', '盗墓小鬼', NULL, 1);
INSERT INTO "unit_yys" VALUES (157, 401, 'N', '寄生魂', NULL, 1);
INSERT INTO "unit_yys" VALUES (158, 403, 'N', '唐纸伞妖', NULL, 1);
INSERT INTO "unit_yys" VALUES (159, 404, 'N', '天邪鬼绿', NULL, 1);
INSERT INTO "unit_yys" VALUES (160, 405, 'N', '天邪鬼赤', NULL, 1);
INSERT INTO "unit_yys" VALUES (161, 406, 'N', '天邪鬼黄', NULL, 1);
INSERT INTO "unit_yys" VALUES (162, 407, 'N', '天邪鬼青', NULL, 1);
INSERT INTO "unit_yys" VALUES (163, 408, 'N', '帚神', NULL, 1);
INSERT INTO "unit_yys" VALUES (164, 409, 'N', '涂壁', NULL, 1);
INSERT INTO "unit_yys" VALUES (165, 414, 'N', '大天狗呱', NULL, 1);
INSERT INTO "unit_yys" VALUES (166, 415, 'N', '酒吞呱', NULL, 1);
INSERT INTO "unit_yys" VALUES (167, 416, 'N', '荒川呱', NULL, 1);
INSERT INTO "unit_yys" VALUES (168, 417, 'N', '阎魔呱', NULL, 1);
INSERT INTO "unit_yys" VALUES (169, 418, 'N', '两面佛呱', NULL, 1);
INSERT INTO "unit_yys" VALUES (170, 419, 'N', '小鹿男呱', NULL, 1);
INSERT INTO "unit_yys" VALUES (171, 420, 'N', '茨木呱', NULL, 1);
INSERT INTO "unit_yys" VALUES (172, 421, 'N', '青行灯呱', NULL, 1);
INSERT INTO "unit_yys" VALUES (173, 422, 'N', '妖刀姬呱', NULL, 1);
INSERT INTO "unit_yys" VALUES (174, 423, 'N', '一目连呱', NULL, 1);
INSERT INTO "unit_yys" VALUES (175, 424, 'N', '花鸟卷呱', NULL, 1);
INSERT INTO "unit_yys" VALUES (176, 425, 'N', '辉夜姬呱', NULL, 1);
INSERT INTO "unit_yys" VALUES (177, 426, 'N', '荒呱', NULL, 1);
INSERT INTO "unit_yys" VALUES (178, 427, 'N', '彼岸花呱', NULL, 1);
INSERT INTO "unit_yys" VALUES (179, 363, 'SSR', '帝释天', NULL, 1);

-- ----------------------------
-- Auto increment value for command_limit
-- ----------------------------
UPDATE "sqlite_sequence" SET seq = 1 WHERE name = 'command_limit';

-- ----------------------------
-- Auto increment value for unit_yys
-- ----------------------------
UPDATE "sqlite_sequence" SET seq = 179 WHERE name = 'unit_yys';

-- ----------------------------
-- Indexes structure for table unit_yys
-- ----------------------------
CREATE UNIQUE INDEX "YysUnit_id_uindex"
ON "unit_yys" (
  "id" ASC
);
CREATE UNIQUE INDEX "YysUnit_unit_id_uindex"
ON "unit_yys" (
  "unit_id" ASC
);

PRAGMA foreign_keys = true;
