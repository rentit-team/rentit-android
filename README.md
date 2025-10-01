<img width="84" height="87" alt="Group 23" src="https://github.com/user-attachments/assets/e4d314cd-f57f-480e-a5a9-42458c0f949c" />  
<br>

# RentIt, 중고 물품 대여 플랫폼
<img width="4960" height="3228" alt="Poster" src="https://github.com/user-attachments/assets/c14b44f5-516c-4d2e-8c40-49a885ab7076" />  
<br><br>   

> RentIt으로 중고 물품을 쉽고 안전하게 대여해요!

<br><br>

## Screens Overview
<table>
  <tr>
    <td align="center"><video loop width="210" src="https://github.com/user-attachments/assets/23ba0b3a-92b7-41cd-a8d8-a8c13c8a7ce6"/></td>
    <td align="center"><video loop width="210" src="https://github.com/user-attachments/assets/2b011e6d-7d0f-4216-b7b1-e66ac40b2085"/></td>
    <td align="center"><video loop width="210" src="https://github.com/user-attachments/assets/599a298a-878b-40fb-9272-ba6ccc872f61"/></td>
    <td align="center"><video loop width="210" src="https://github.com/user-attachments/assets/f409fcf9-0314-4d8b-be7b-8a6dfb6671b5"/></td>
  </tr>
  <tr>
    <th>홈</th>
    <th>채팅</th>
    <th>마이페이지</th>
    <th>게시글 작성</th>
  </tr>
  <tr>
    <td width="25%"></td>
    <td width="25%"><ul>
      <li>STOMP 프로토콜을 활용한 WebSocket 기반 실시간 채팅 연결 및 메시지 송수신</li>
    </ul></td>
    <td width="25%"></td>
    <td width="25%"><ul>
      <li>다중 이미지 선택용 런처</li>
      <li>StateFlow로 카테고리 선택 상태 관리</li>
    </ul></td>
  </tr>
</table>
<br>

## Rental Process Overview
> 대여 프로세스 흐름
<img width="500" src="https://github.com/user-attachments/assets/efbaa0ff-05bf-493f-a79b-a35cae07e7d3" />

**1. [대여 요청]**

<table>
  <tr>
    <td>사용자 화면</td>
  </tr>
  <tr>
    <td align="center"><video loop width="400" src="https://github.com/user-attachments/assets/1a2a3d89-dbb3-4be6-b9fc-f7b4cd9dfc7a"/></td>
  </tr>
</table><br>  
      
**2. [대여 수락] 및 채팅**

<table>
  <tr>
    <td>대여자 화면</td>
  </tr>
  <tr>
    <td align="center"><video loop width="400" src="https://github.com/user-attachments/assets/fefc39a6-2c05-4192-8bfd-8647fb65c8bf"/></td>
  </tr>
</table><br>

**3. [결제 완료] 및 채팅**

<table>
  <tr>
    <td>사용자 화면</td>
  </tr>
  <tr>
    <td align="center"><video loop width="400" src="https://github.com/user-attachments/assets/6b29bec6-501a-4d56-a6a2-40c6734f7cc7"/></td>
  </tr>
</table><br>  
      
**3. 대여 전 사진 등록**

<table>
  <tr>
    <td>대여자 화면</td>
  </tr>
  <tr>
    <td align="center"><video loop width="400" src="https://github.com/user-attachments/assets/f1b634f9-a83c-40f7-845a-d29251d69dc5"/></td>
  </tr>
</table><br>  
      
**4. [대여 중] (반납 전 사진 등록) 및 [반납 완료]**

<table>
  <tr>
    <td>사용자 화면</td>
  </tr>
  <tr>
    <td align="center"><video loop width="400" src="https://github.com/user-attachments/assets/2d382155-9ca5-4947-86e8-364eae6901eb"/></td>
  </tr>
</table><br>

## Tech Stack
- Kotlin
- Jetpack Compose
- MVVM, MVI
- Retrofit2
- Hilt
- StateFlow
- Coroutine
<br><br>

## Commit Type
- **add** : 새로운 파일, 코드 추가
- **feat** : 새로운 기능 추가, 기존의 기능을 요구 사항에 맞추어 수정
- **fix** : 기능에 대한 버그 수정
- **chore** : 패키지 매니저 수정, 잡일, 그 외 기타 수정 ex) .gitignore
- **docs** : 문서(주석) 수정
- **style** : 코드 스타일, 포맷팅에 대한 수정
- **refactor** : 기능의 변화가 아닌 코드 리팩터링 ex) 변수 이름 변경
- **ui** : 새로운 UI 생성
<br><br>

## UI Design
- Design System
<img height="1024" alt="image" src="https://github.com/user-attachments/assets/9e53fbef-9cd8-4fd9-bcae-3c6922effef3" />

- UI Design
<img height="1024" alt="image" src="https://github.com/user-attachments/assets/dd32a142-a63c-4d9d-84ce-31e4036a5351" />
