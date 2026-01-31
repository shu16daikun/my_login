# my_login — モジュールマップ（JPMS無効 / ルートpkg = `com.login`）

> 目的：認証・ユーザー・権限の基盤。**外部公開は API（`api/*`）のみ**。Entity/Repository 直参照は不可。

---

## 1) 公開インターフェース（他モジュールが呼べるもの）

### Authority コンポーネント（`components/authority/api/*`）

* **Domain**

  * `MyAuthorityEnum` … 公開する権限セット（ROLE など）。
* **DTO**

  * `MyAuthorityInputDto`, `MyAuthorityViewDto` … 権限系ユースケースの入出力。
* **Param**

  * `MyAuthorityObjParam` … コントローラ境界のバインド用オブジェクト。
* **Service（契約）**

  * `MyAuthorityService` … 権限情報取得などの公開インターフェース。

### User コンポーネント（`components/user/api/*`）

* **DTO**

  * 入力：`MyLoginInputDto`, `MyPasswordSetInputDto`, `MyUsersInputDto`
  * 出力：`MyPasswordSetViewDto`, `MyUsersViewDto`
* **例外（API表面）**

  * `MyPasswordSetException`, `MyUsersException`
* **Param**

  * `MyPasswordSetObjParam`, `MyUsersObjParam`
* **Service（契約）**

  * `MyPasswordSetService`, `MyUsersService`

> 補足：モジュール起動用のエントリー（主にテスト/ツール）に `SpringLoginModuleApplication` あり。

---

## 2) 内部（`internal/*`）

原則 **package-private**。AOP 対象クラス（`@Service/@Repository/@Component`）は **public 非final**。

### セキュリティ連携

* `MyUserDetails`, `MyUserDetailsServiceImpl` … Spring Security のユーザー読み出しブリッジ。

### User コンポーネント内部

* **Services**：`MyUsersServiceImpl`, `PasswordSetServiceImpl`（`@PreAuthorize` / `@Transactional` / `private toEntity()`）
* **Errors**：`UsersErrorCode`, `PasswordSetErrorCode`
* **Persistence**：`UsersEntity`, `UsersRepository`, `UsersDB`
* **ID/Bridge**：`UsersIdBridge`, `UsersIdConstants`
* **Mappers**：`ToMyUsersViewDtoMapper`, `ToMyPasswordSetViewDtoMapper`

### Authority コンポーネント内部

* **Persistence**：`MyAuthorityEntity`, `MyAuthorityRepository`, `MyAuthorityDB`
* **Service**：`MyAuthorityServiceImpl`
* **ID/Errors**：`MyAuthorityIdBridge`, `MyAuthorityIdConstants`, `MyAuthorityErrorCode`
* **Mappers**：`ToMyAuthorityEnumMapper`, `ToMyAuthorityViewDtoMapper`

---

## 3) モジュール横断ユーティリティ／リソース

* **PropKey（モジュール用キー集約）**：`util/param/MyPropKey`
* **Resources**：`application.properties`（既定値）、`my_login/config/error.properties`（エラーメッセージ）

---

## 4) 他モジュールからの使い方（お作法）

* 依存は **`api/*` のみ**（DTO/Service IF）。`internal/*` への直参照は禁止。
* 親アプリのコンポーネントスキャンに **`com.login`** を含める。
* 呼び出すサービス：`MyAuthorityService`／`MyUsersService`／`MyPasswordSetService`。

---

## 5) 命名とレイヤ分離（クイック規約）

* 公開 API：`My<Comp><UseCase>InputDto`／`My<Comp>ViewDto`／`My<Comp>Service`
* 実装：`<Comp>ServiceImpl` は **public 非final**、その他は package-private
* ID：`*IdBridge` + `*IdConstants`
* 変換：`To<変換先>Mapper.from<変換元>(...)`
* エラー：`<Comp>ErrorCode`（必要なら `api/exception` で公開例外）

---

## 6) 参考：主な配置（抜粋）

* Authority の API / internal：`components/authority/...`
* User の API / internal：`components/user/...`
* ルート & リソース：モジュール直下と `resources/`

---
