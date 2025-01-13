-- 데이터베이스 생성
DROP DATABASE IF EXISTS bbip_db;
CREATE DATABASE IF NOT EXISTS bbip_db;
USE bbip_db;

-- 'user' 테이블 생성
CREATE TABLE IF NOT EXISTS `user` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,  -- 'id' 필드, BIGINT 타입으로 자동 증가
    `email` VARCHAR(255) NOT NULL,           -- 'email' 필드, 암호화된 이메일 저장 (VARCHAR 255)
    `name` VARCHAR(20) NOT NULL,             -- 'name' 필드, 사용자의 이름 (VARCHAR 20)
    `nickname` VARCHAR(20) NOT NULL,
    `oauth_provider` VARCHAR(100),            -- 'oauthProvider' 필드, OAuth 제공자 정보 (VARCHAR 100)
    `deleted` BOOLEAN NOT NULL DEFAULT false -- 'deleted' 필드, 소프트 삭제 처리 필드 (BOOLEAN)
);

-- 'face' 테이블 생성
CREATE TABLE IF NOT EXISTS `face` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,  -- 고유 식별자 추가
    `user_id` INT NOT NULL,
    `file_url` VARCHAR(255) NOT NULL,
    `file_name` VARCHAR(255) NOT NULL,
    `face_embedding` BLOB NOT NULL,
    `self` TINYINT(1) NOT NULL,
    CONSTRAINT `fk_face_user`
    FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
    ON DELETE CASCADE
);

-- 'rtmp_server' 테이블 생성
CREATE TABLE IF NOT EXISTS `rtmp_server` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(20) NOT NULL,
    `uri` VARCHAR(100) NOT NULL
);

-- 'stream_key' 테이블 생성
CREATE TABLE IF NOT EXISTS `stream_key` (
    `user_id` INT NOT NULL,
    `server_id` INT NOT NULL,
    `key` VARCHAR(100) NOT NULL,
    `stream` BOOLEAN NOT NULL DEFAULT false,
    PRIMARY KEY (`user_id`, `server_id`),
    CONSTRAINT `fk_streamkey_user`
    FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
    ON DELETE CASCADE,
    CONSTRAINT `fk_streamkey_server`
    FOREIGN KEY (`server_id`) REFERENCES `rtmp_server` (`id`)
    ON DELETE CASCADE
);

INSERT INTO `rtmp_server` (`id`, `name`, `uri`) VALUES
    (1, 'youtube', 'rtmp://a.rtmp.youtube.com/live2/'),
    (2, 'twitch', 'rtmp://live.twitch.tv/app/'),
    (3, 'afreeca_tv', 'rtmp://afreecatv.com/live/'),
    (4, 'chzzk', 'rtmp://global-rtmp.lip2.navercorp.com:8080/relay'),
    (5, 'periscope', 'rtmp://live.periscope.tv:80/'),
    (6, 'facebook', 'rtmp://live-api-s.facebook.com:80/rtmp/'),
    (7, 'dLive', 'rtmp://stream.dlive.tv/'),
    (8, 'trovo', 'rtmp://live.trovo.live/stream/');

SELECT * from user;
INSERT INTO user values (1, 'yukyj0418@gmail.com', '육예진', '육예진', 'google', false);