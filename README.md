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

### Git Flow 전략
- **Main 브랜치**   
Main 브랜치는 출시 가능한 프로덕션 코드를 모아두는 브랜치이다. Main 브랜치는 프로젝트 시작 시 생성되며, 개발 프로세스 전반에 걸쳐 유지된다. 배포된 각 버전을 Tag를 이용해 표시해둔다.

- **Develop 브랜치**   
다음 버전 개발을 위한 코드를 모아두는 브랜치이다. 개발이 완료되면, Main 브랜치로 머지된다.

- **Feature 브랜치**   
하나의 기능을 개발하기 위한 브랜치이다. Develop 브랜치에서 생성하며, 기능이 개발 완료되면 다시 Develop 브랜치로 머지된다. 머지할때 주의점은 Fast-Forward로 머지하지 않고, Merge Commit을 생성하며 머지를 해주어야 한다. 이렇게해야 히스토리가 특정 기능 단위로 묶이게 된다.   
네이밍은 feature/branch-name 과 같은 형태로 생성한다.

- **Release 브랜치**   
소프트웨어 배포를 준비하기 위한 브랜치이다. Develop 브랜치에서 생성하며, 버전 이름 등의 소소한 데이터를 수정하거나 배포전 사소한 버그를 수정하기 위해 사용된다. 배포 준비가 완료되었다면 Main과 Develop 브랜치에 둘다 머지한다. 이때, Main 브랜치에는 태그를 이용하여 버전을 표시한다.
네이밍은 release/v1.1 과 같은 형태로 생성한다.

- **Hotfix 브랜치**   
이미 배포된 버전에 문제가 발생했다면, Hotfix 브랜치를 사용하여 문제를 해결한다. Main 브랜치에서 생성하며, 문제 해결이 완료되면 Main과 Develop 브랜치에 둘다 머지한다.
네이밍은 hotfix/v1.0.1 과 같은 형태로 생성한다.
