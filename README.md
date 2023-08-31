# Dopamine Backend

## 🫧git convention

### Commit Style

- Commit 메세지 형식

  **<type> -#<이슈번호> : <제목>**

  **<본문>**


- Commit 메세지 예시

  **feat -#123 : CRUD 기능 추가**

  **고객 등록 페이지의 CRUD 기능 추가**

- commit type
    - feat : 새로운 기능에 대한 커밋
    - fix : build 빌드 관련 파일 수정에 대한 커밋
    - build : 빌드 관련 파일 수정에 대한 커밋
    - chore : 그 외 자잘한 수정에 대한 커밋(rlxk qusrud)
    - ci : CI 관련 설정 수정에 대한 커밋
    - docs : 문서 수정에 대한 커밋
    - style : 코드 스타일 혹은 포맷 등에 관한 커밋
    - refactor : 코드 리팩토링에 대한 커밋
    - test : 테스트 코드 수정에 대한 커밋
- Commit 메세지 규칙
    1. 제목의 끝에는 마침표를 적지 않습니다.
    2. 제목은 명령문으로 적습니다.(과거형을 사용하지 않습니다.)
    3. 어떻게 보다는 무엇과 왜를 설명합니다.
- Push와 Pull Request방식 - github flow방식 이용(항상 master 브랜치는 CI/CD가 되도록 유지)
    - Push : 새로 만든 브랜치에 push 하기
    - Pull Request : repository또는 브랜치에서 **master 브랜치**로 pull request로 merge하기

### Branch Name Style

- 기능별로 브랜치를 생성하고, 1개의 브랜치는 1명의 사용자가 담당합니다.

```
commit_type/이름/기능명

feat/gabang2/login
feat/gabang2/logo
```