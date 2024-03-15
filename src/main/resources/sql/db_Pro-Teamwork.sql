use proj_teamwork_db;

drop table if exists `t_project`;
drop table if exists `t_group_project`;
drop table if exists `t_project_user`;
drop table if exists `t_requirement`;
drop table if exists `t_requirement_iteration`;
drop table if exists `t_iteration`;
drop table if exists `t_bug`;
drop table if exists `t_bug_type`;
drop table if exists `t_user_role`;
drop table if exists `t_user_group`;
drop table if exists `t_user`;
drop table if exists `t_role_group`;
drop table if exists `t_group`;
drop table if exists `t_power_role`;
drop table if exists `t_role`;
drop table if exists `t_power`;
drop table if exists `t_power_type`;

create table `t_user`
(
    `id`            bigint      not null primary key,
    `username`      varchar(20) not null comment '用户名',
    `passwd`        char(70)    not null comment '密码',
    `email`         varchar(50) not null comment '邮箱',
    `enable`        int         not null comment '启用',
    `deleted`       int         not null default 0,
    `version`       int         not null default 0,
    constraint t_user_unique unique (username, deleted)
) comment '用户';

create table `t_group`
(
    `id`      bigint      not null primary key,
    `name`    varchar(30) not null comment '用户组名',
    `enable`  int         not null comment '启用',
    `deleted` int         not null default 0,
    `version` int         not null default 0,
    constraint t_group_unique unique (name, deleted)
) comment '用户组';

create table `t_user_group`
(
    `id`       bigint not null primary key,
    `user_id`  bigint not null comment '用户',
    `group_id` bigint not null comment '用户组',
    `deleted`  int    not null default 0,
    `version`  int    not null default 0,
    constraint t_user_group_user_id_fk foreign key (user_id) references t_user (id),
    constraint t_user_group_group_id_fk foreign key (group_id) references t_group (id)
) comment '中间表-用户-用户组';

create table `t_role`
(
    `id`      bigint      not null primary key,
    `name`    varchar(20) not null comment '角色名',
    `enable`  int         not null comment '启用',
    `deleted` int         not null default 0,
    `version` int         not null default 0,
    constraint t_role_unique unique (name, deleted)
) comment '角色';

create table `t_role_group`
(
    `id`       bigint not null primary key,
    `role_id`  bigint not null comment '角色',
    `group_id` bigint not null comment '用户组',
    `deleted`  int    not null default 0,
    `version`  int    not null default 0,
    constraint t_role_group_role_id_fk foreign key (role_id) references t_role (id),
    constraint t_role_group_group_id_fk foreign key (group_id) references t_group (id)
) comment '中间表-角色-用户组';

create table `t_user_role`
(
    `id`      bigint not null primary key,
    `user_id` bigint not null comment '用户',
    `role_id` bigint not null comment '角色',
    `deleted` int    not null default 0,
    `version` int    not null default 0,
    constraint t_user_role_user_id_fk foreign key (user_id) references t_user (id),
    constraint t_user_role_role_id_fk foreign key (role_id) references t_role (id)
) comment '中间表-用户-角色';

create table `t_power_type`
(
    `id`      bigint      not null primary key auto_increment,
    `name`    varchar(50) not null comment '权限类型名',
    `deleted` int         not null default 0,
    `version` int         not null default 0
) comment '权限类型';

create table `t_power`
(
    `id`      bigint not null primary key auto_increment,
    `type_id` bigint not null comment '权限类型',
    `deleted` int    not null default 0,
    `version` int    not null default 0,
    constraint t_power_type_id_fk foreign key (type_id) references t_power_type (id)
) comment '权限';

create table `t_power_role`
(
    `id`       bigint not null primary key,
    `power_id` bigint not null comment '权限',
    `role_id`  bigint not null comment '角色',
    `deleted`  int    not null default 0,
    `version`  int    not null default 0,
    constraint t_power_role_power_id_fk foreign key (power_id) references t_power (id),
    constraint t_power_role_role_id_fk foreign key (role_id) references t_role (id)
) comment '中间表-权限-角色';

create table `t_project`
(
    `id`          bigint      not null primary key,
    `name`        varchar(30) not null comment '项目名',
    `description` text        not null comment '描述',
    `status`      int         not null default 0 comment '项目状态',
    `start_time`  datetime    not null default (utc_timestamp()) comment '开始日期',
    `end_time`    datetime    not null comment '结束时间',
    `creator_id`     bigint      not null comment '创建人',
    `deleted`     int         not null default 0,
    `version`     int         not null default 0,
    constraint t_project_creator_id_fk foreign key (creator_id) references t_user (id)
) comment '项目';

create table `t_group_project`
(
    `id`         bigint not null primary key,
    `project_id` bigint not null comment '项目',
    `group_id`   bigint not null comment '用户组',
    `deleted`    int    not null default 0,
    `version`    int    not null default 0,
    constraint t_group_project_project_id_fk foreign key (project_id) references t_project (id),
    constraint t_group_project_group_id_fk foreign key (group_id) references t_group (id)
) comment '中间表-用户组-项目';

create table `t_project_user`
(
    `id`         bigint not null primary key,
    `project_id` bigint not null comment '项目',
    `user_id`    bigint not null comment '用户',
    `deleted`    int    not null default 0,
    `version`    int    not null default 0,
    constraint t_project_user_project_id_fk foreign key (project_id) references t_project (id),
    constraint t_project_user_user_id_fk foreign key (user_id) references t_user (id)
) comment '中间表-用户-项目';

create table `t_iteration`
(
    `id`           bigint       not null primary key,
    `name`         varchar(30)  not null comment '迭代名称',
    `description`  text         not null comment '描述',
    `status`       int          not null default 0 comment '迭代状态',
    `start_time`   datetime     not null default (utc_timestamp())comment '开始日期',
    `end_time`     datetime     not null comment '结束日期',
    `director_id`  bigint       not null comment '负责人',
    `project_id`   bigint       not null comment '所属项目',
    `deleted`      int          not null default 0,
    `version`      int          not null default 0,
    constraint t_iteration_director_id_fk foreign key (director_id) references t_user (id),
    constraint t_iteration_project_id_fk foreign key (project_id) references t_project (id)
) comment '迭代';


create table `t_task`
(
    `id`           bigint      not null primary key,
    `title`        varchar(30) not null comment '标题',
    `description`  text        not null comment '描述',
    `status`       int         not null default 0 comment '任务状态',
    `priority`     int         not null default 1 comment '优先级',
    `create_time`  datetime    not null default (utc_timestamp()) comment '创建日期',
    `modify_time`  datetime    not null default (utc_timestamp()) comment '上次更新日期',
    `end_time`     datetime    not null comment '结束日期',
    `creator_id`   bigint      not null comment '创建人',
    `processer_id` bigint      not null comment '处理人',
    `iteration_id` bigint      not null comment '所属迭代',
    `project_id`   bigint      not null comment '所属项目',
    `deleted`      int         not null default 0,
    `version`      int         not null default 0,
    constraint t_task_creator_id_fk foreign key (creator_id) references t_user (id),
    constraint t_task_processer_id_fk foreign key (processer_id) references t_user (id),
    constraint t_task_iteration_id_fk foreign key (iteration_id) references t_iteration (id),
    constraint t_task_project_id_fk foreign key (project_id) references t_project (id)
) comment '任务';


create table `t_requirement`
(
    `id`           bigint      not null primary key,
    `title`        varchar(30) not null comment '标题',
    `description`  text        not null comment '描述',
    `status`       int         not null default 0 comment '需求状态',
    `priority`     int         not null default 1 comment '优先级',
    `create_time`  datetime    not null default (utc_timestamp()) comment '创建日期',
    `modify_time`  datetime    not null default (utc_timestamp()) comment '上次更新日期',
    `creator_id`   bigint      not null comment '创建人',
    `processer_id` bigint      not null comment '处理人',
    `iteration_id` bigint      not null comment '所属迭代',
    `project_id`   bigint      not null comment '所属项目',
    `origin`       varchar(30) not null comment '需求来源',
    `deleted`      int         not null default 0,
    `version`      int         not null default 0,
    constraint t_requirement_creator_id_fk foreign key (creator_id) references t_user (id),
    constraint t_requirement_processer_id_fk foreign key (processer_id) references t_user (id),
    constraint t_requirement_iteration_id_fk foreign key (iteration_id) references t_iteration (id),
    constraint t_requirement_project_id_fk foreign key (project_id) references t_project (id)
) comment '需求';

create table `t_bug_type`
(
    `id`          bigint      not null primary key auto_increment,
    `name`        varchar(10) not null comment '缺陷类型名',
    `deleted`     int         not null default 0,
    `version`     int         not null default 0
) comment '缺陷类型';

create table `t_bug`
(
    `id`             bigint      not null primary key,
    `title`          varchar(30) not null comment '标题',
    `description`    text        not null comment '描述',
    `status`         int         not null default 0 comment '缺陷状态',
    `priority`       int         not null default 1 comment '优先级',
    `level`          int         not null default 1 comment '严重程度',
    `reappear`       int         not null default 1 comment '重现规律',
    `create_time`    datetime    not null default (utc_timestamp()) comment '创建日期',
    `modify_time`    datetime    not null default (utc_timestamp()) comment '上次更新日期',
    `creator_id`     bigint      not null comment '创建人',
    `processer_id`   bigint      not null comment '处理人',
    `bug_type_id`    bigint      not null comment '缺陷类型',
    `project_id`     bigint      not null comment '所属项目',
    `discovery_iterate_id`   bigint      not null comment '缺陷发现迭代',
    `plan_iterate_id`   bigint      not null comment '规划迭代',
    `requirement_id` bigint      not null comment '关联需求',
    `origin`         varchar(30) not null comment '缺陷来源',
    `deleted`        int         not null default 0,
    `version`        int         not null default 0,
    constraint t_bug_creator_id_fk foreign key (creator_id) references t_user (id),
    constraint t_bug_processer_id_fk foreign key (processer_id) references t_user (id),
    constraint t_bug_type_id_fk foreign key (bug_type_id) references t_bug_type (id),
    constraint t_bug_project_id_fk foreign key (project_id) references t_project (id),
    constraint t_bug_discovery_iterate_id_fk foreign key (discovery_iterate_id) references t_iteration (id),
    constraint t_bug_plan_iterate_id_fk foreign key (plan_iterate_id) references t_iteration (id),
    constraint t_bug_requirement_id_fk foreign key (requirement_id) references t_requirement (id)
) comment '缺陷';
