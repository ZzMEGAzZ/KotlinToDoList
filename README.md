# Planning To Do List App
  แอพ To do list เป็นแอพการบันทึกสิงที่ต้องทำให้กับผู้ใช้งาน มีฟีเจอร์ดังนี้
  - Sign-In
  - Sign-Up
  - Show Task list(To-do)
  - Show Done list(Done task)
  - Add Task
  - Edit Task
  - Delete Task
  - Log-out

## About source code 
- Language: Kotlin
- Front: XML
- Data base: Firebase
- ในฟังก์ชันแต่ละหน้า จะแบ่งเป็น Fragment

 ### FinishedFragment หน้าแสดง Done task
 
 ### HomeFragment หน้าแสดง To do list ทั้งหมดที่ยังไม่เสร็จ
  -  รับผิดชอบในการแสดงรายการสิ่งที่ผู้ใช้ต้องทำ (to-do tasks) โดยมีการติดต่อกับ Firebase เพื่อดึงข้อมูลและอัปเดตงานที่ต้องทำ
    ## ฟังก์ชัน
      - onCreateView: ใช้ในการเตรียม layout สำหรับ fragment
      - onViewCreated: ทำการเริ่มต้นคอมโพเนนต์ UI, ตั้งค่า listener สำหรับคลิกปุ่ม, และดึงงานที่ต้องทำจาก Firebase
      - getTaskFromFirebase: ดึงงานที่ต้องทำจาก Firebase Realtime Database และอัปเดต UI
      - init: เตรียมคอมโพเนนต์ที่จำเป็น เช่น Firebase, authentication, และ recycler view

### FinishedFragment หน้าแสดง To do list ทั้งหมดที่ยังเสร็จแล้ว
  -  แสดงรายการงานที่ผู้ใช้ทำเสร็จแล้ว โดยมีการติดต่อกับ Firebase เพื่อดึงข้อมูลและอัปเดตงานที่เสร็จสิ้น
    ## ฟังก์ชัน
      - onCreateView: ใช้ในการเตรียม layout สำหรับ fragment
      - onViewCreated: ทำการเริ่มต้นคอมโพเนนต์ UI, ตั้งค่า listener สำหรับคลิกปุ่ม, และดึงงานที่เสร็จสิ้นจาก Firebase
      - getTaskFromFirebase: ดึงงานที่เสร็จสิ้นจาก Firebase Realtime Database และอัปเดต UI
      - init: เตรียมคอมโพเนนต์ที่จำเป็น เช่น Firebase, authentication, และ recycler view

 
 ### SingInFragment หน้า Sign In ของ app 
 
 ### SignUpFragment หน้า Sing up ของ app
 
 ### SplashFragment หน้าแรกที่แสดง Logo ก่อนหน้า Login
 
 ### ToDoDialogFragment หน้า Pop up ในการ Add task
   - ToDoDialogFragment เป็น dialog fragment ที่ใช้สำหรับเพิ่มและแก้ไขงาน มีการสื่อสารกับ fragment ที่เรียกใช้ผ่าน OnDialogNextBtnClickListener interface
     ## ฟังก์ชัน
      - onCreateView: ใช้ในการเตรียม layout สำหรับ dialog fragment
      - onViewCreated: ทำการเริ่มต้นคอมโพเนนต์ UI และจัดการตรรกะสำหรับการเพิ่มหรือแก้ไขงาน
      - setListener: ตั้งค่า listener สำหรับคลิกปุ่มใน dialog
      - newInstance: สร้าง instance ใหม่ของ dialog fragment พร้อมข้อมูลงานที่เป็นไปได้
      - saveTask: บันทึกงานใหม่ไปยัง Firebase
      - updateTask: อัปเดตงานที่มีอยู่ใน Firebase
     
## Picture Demo

### Sign up
![singup](https://github.com/ZzMEGAzZ/KotlinToDoList/assets/88434297/fc5ff7f8-e9f1-4390-835b-ea61fcd44821)
### Sign In
![singin](https://github.com/ZzMEGAzZ/KotlinToDoList/assets/88434297/f6f5f7d2-471b-4ef4-8f62-bc45bde32427)
#### Home
![home](https://github.com/ZzMEGAzZ/KotlinToDoList/assets/88434297/223d82fb-f6d1-4359-8402-cb07053ab8b7)
### Add Task
![add](https://github.com/ZzMEGAzZ/KotlinToDoList/assets/88434297/2612717f-b875-468b-b996-1d54db010cb2)
### Done List
![done](https://github.com/ZzMEGAzZ/KotlinToDoList/assets/88434297/7eff87c6-ee8b-414c-8beb-62082ccf9b11)

## Team Members
1. **Apisit Sangkrajang** (ID: 6410742016)
2. **Panuwat Mangkang** (ID: 6410742057)
3. **Nutchapon Kitkram** (ID: 6410742156)

## Have fun using the To Do List App!
