# Ostadi (أستاذي) – Senior Android QA Audit & Production Hardening Plan

Comprehensive functionality audit, arithmetic validation, multi-tenant security verification, and automated test suite for the Ostadi Algerian teacher application.

## User Review & Critical Decisions

> [!IMPORTANT]
> The following architectural decisions have been synthesized from your explicit feedback and requirements:

- **Confirmed Decision 1 (Demo Mode vs. Production Auth)**: The application maintains a clearly marked Demo Preview mode with read-only sample Algerian classrooms and students so unauthenticated teachers can explore the UI without friction. Authenticated mode strictly connects to Cloud Firestore (`teachers/{teacherUid}`) and prevents any sample data leakage into production databases.
- **Confirmed Decision 2 (Algerian Grade Weighting & Missing Grades)**: Grade calculations strictly follow the official Algerian educational formula: continuous assessment (التقويم المستمر), control test (الفرض المحروس), and term exam (الاختبار الفصلي, coefficient 2). Crucially, unsubmitted or pending grades (`null`) are **never treated as zero**; denominators and weights adjust dynamically based on completed components only, ensuring averages are not artificially deflated.
- **Confirmed Decision 3 (Teacher Privacy & Multi-Tenancy)**: Teacher data is strictly partitioned by Firebase Authentication UID (`teachers/{teacherUid}/...`). Firestore security rules and repository listeners guarantee zero data leakage between independent teachers.
- **Confirmed Decision 4 (Comprehensive Automated Test Evidence)**: Every verification item will be validated via rigorous local JVM Robolectric unit tests (`ExampleRobolectricTest.kt` / test suites) covering multi-teacher isolation, arithmetic edge cases, attendance denominator accuracy, and UI state integrity.

---

## 1. Overview & Core Concept

- **What It Does**: Ostadi (أستاذي) is a native Android application built in Kotlin and Jetpack Compose designed specifically for primary, middle (CEM), and secondary (Lycée) teachers in Algeria. It enables teachers to manage classrooms, track student rosters, record daily session attendance, compute official term grades with weighted coefficients, award participation stars, and view real-time educational analytics.
- **Target Audience / Persona**: Independent Algerian educators managing between 1 and 6 classes (30–250 students total) who need a fast, private, and offline-resilient tool formatted in Arabic (with full RTL support) and optional French.
- **Key Value**: Replaces paper gradebooks and ad-hoc spreadsheets with a secure, automated, and mathematically exact mobile assistant compliant with Algerian Ministry of National Education guidelines.

---

## 2. User Experience & Visual Design

### Key User Flows

1. **Launch & Authentication Gate**:
   - The user opens the app to the **Welcome Screen**.
   - Teachers can either tap **"معاينة تجريبية" (Demo Preview)** to inspect the app with pre-populated Algerian sample data, or tap **"تسجيل الدخول" / "حساب جديد"** to authenticate via real Firebase Authentication (Email/Password or Google Sign-In via Credential Manager).
   - Once authenticated, the app streams real, private data from `teachers/{teacherUid}`.

2. **Dashboard & Metric Overview**:
   - Displays real, computed statistics cards: Total Classrooms, Total Students, Male vs. Female distribution bar, Today's Absences, Upcoming Exams, and Class Averages.
   - When the database has zero records, clean empty-state cards prompt the teacher to create their first classroom rather than displaying hardcoded numbers.

3. **Classroom & Student Roster Management**:
   - View classrooms filtered by educational level (CEM 1AM–4AM, Lycée 1AS–3AS, Primary).
   - Add students with first name, last name, gender (ذكر / أنثى), registration number, and parent phone.
   - Live roster updates propagate immediately across all dashboard statistics and attendance lists.

4. **Session Attendance Tracking**:
   - Select date and timetable slot (e.g., الحصة 1: 08:00 - 09:00).
   - One-tap status toggles: **حاضر (Present)**, **غائب (Absent)**, **متأخر (Late)**, and **مبرر (Excused)**.
   - Attendance percentage uses only non-excused instructional sessions as denominator.

5. **Grades & Official Algerian Coefficients**:
   - Enter continuous evaluation (التقويم), tests (الفرض), and exams (الاختبار).
   - Supports nullable grades: empty fields display "غير مرصود" (Pending) and are omitted from calculation rather than penalized with zero.
   - Automatic mention determination (ممتاز, جيد جداً, جيد, مقبول, دون المتوسط).

6. **Profile & Language Switching**:
   - Instant language switch between **العربية (RTL)** and **Français (LTR)** with immediate layout direction inversion.
   - Profile management with school name, wilaya, and subject specification.
   - Secure Sign Out button returning to the authentication gate.

### Visual Identity & Theme Tokens

- **Palette**: Deep Algerian Educational Navy (`#0F2042` / `NavyPrimary`), Accent Gold/Amber (`#D4A766` / `GoldAccent`), Clean Container White (`#FFFFFF`), Soft Navy Surface (`#F4F7FC`), and Semantic Alerts (Emerald `#2E7D32` for Presence, Crimson `#C62828` for Absence, Amber `#F57C00` for Late).
- **Typography**: Clean, high-legibility Arabic typography with proper line-height and weight balance (Cairo/Almarai aesthetic) scaling smoothly with Android accessibility font scale settings.
- **RTL & Density**: Generous 16dp edge padding, 12dp card rounding, 48dp minimum touch targets, and full bidirectional symmetry (`LocalLayoutDirection`).

---

## 3. Key Product Decisions & Trade-Offs

- **Decision 1: Nullable Grade Scoring vs. Default Zero**:
  - *Chosen Approach*: Represent missing grade inputs as `Double? = null`.
  - *Why*: In Algerian schools, tests and exams occur at different dates across the term. Treating an unentered exam as `0.0/20` ruins the student's term average and panics parents/students. Missing components are omitted from weighted denominators until recorded.
  - *Alternatives Considered*: Defaulting to `0.0` was rejected because it violates Algerian academic grading standards.

- **Decision 2: Dynamic Attendance Denominators**:
  - *Chosen Approach*: Calculate attendance rate as:
    $$\text{Rate} = \frac{\text{Present Sessions} + \text{Late Sessions}}{\text{Total Sessions Recorded for that Student}} \times 100$$
    Excused absences are tracked separately and excluded from negative penalty calculations.
  - *Why*: Accurately reflects actual class presence without penalizing medical leaves or school holidays.

- **Decision 3: Isolated Dual-Mode Repository Pattern**:
  - *Chosen Approach*: A unified `TeacherRepository` interface backed by two implementations:
    1. `FirestoreTeacherRepository`: Live, authenticated, encrypted Firestore streaming for real accounts.
    2. `SampleTeacherRepository`: Local in-memory repository used strictly during "Demo Mode".
  - *Why*: Guarantees demo data never accidentally writes to Firestore, while enabling prospective teachers to test drive the app instantly.

- **Decision 4: Multi-Tenant Zero-Trust Security**:
  - *Chosen Approach*: Enforce document path matching `match /teachers/{teacherUid}/{allSubcollections=**}` guarded by `request.auth.uid == teacherUid`.
  - *Why*: Even if a malicious client attempts to forge document IDs of another teacher, Firestore rules reject read and write requests at the transport layer.

---

## 4. Technical Architecture & Data Strategy

### System Architecture Diagram

```
┌────────────────────────────────────────────────────────────────────────┐
│                     Jetpack Compose UI Layer                          │
│                                                                        │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  ┌────────────┐  │
│  │ Welcome/Auth │  │  Dashboard   │  │  Classrooms  │  │  Students  │  │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘  └─────┬──────┘  │
│         │                 │                 │                │         │
│  ┌──────┴───────┐  ┌──────┴───────┐  ┌──────┴───────┐        │         │
│  │  Attendance  │  │    Grades    │  │  Statistics  │        │         │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘        │         │
└─────────┼─────────────────┼─────────────────┼────────────────┼─────────┘
          │                 │                 │                │
          ▼                 ▼                 ▼                ▼
┌────────────────────────────────────────────────────────────────────────┐
│              OstadiViewModel & OstadiUiState (StateFlow)              │
│  - Mathematical grade evaluator (nullable, weighted)                  │
│  - Real-time aggregation (male/female, absences, class averages)      │
│  - Mode switch (Demo Preview vs. Authenticated Firestore)             │
└───────────────────────────────────┬────────────────────────────────────┘
                                    │
                                    ▼
┌────────────────────────────────────────────────────────────────────────┐
│                 TeacherRepository Interface (Domain Layer)             │
└───────────────────┬───────────────────────────────┬────────────────────┘
                    │                               │
          [Demo Mode Active]              [Authenticated]
                    │                               │
                    ▼                               ▼
┌───────────────────────────────────┐ ┌──────────────────────────────────┐
│     SampleTeacherRepository       │ │    FirestoreTeacherRepository    │
│  - In-memory realistic Algerian   │ │  - Collection: teachers/{uid}    │
│    CEM demo data (read-only)      │ │  - Subcollections: classrooms,   │
│  - Zero cloud network calls       │ │    students, attendance, grades  │
└───────────────────────────────────┘ └─────────────────┬────────────────┘
                                                        │
                                                        ▼
                                      ┌──────────────────────────────────┐
                                      │    Cloud Firestore & Security    │
                                      │  - Rule: request.auth.uid == uid │
                                      │  - Zero cross-tenant leakage     │
                                      └──────────────────────────────────┘
```

### Data Model & Arithmetic Specifications

1. **Grade Record Entity (`GradeRecord.kt`)**:
   - `continuousAssessment: Double?` (Optional / Nullable)
   - `testScore: Double?` (Optional / Nullable)
   - `examScore: Double?` (Optional / Nullable, coefficient 2)
   - **Mathematical Computation**:
     - If all three present: `average = (((continuous + test) / 2.0) + (exam * 2.0)) / 3.0`
     - If continuous + test only: `average = (continuous + test) / 2.0`
     - If test + exam only: `average = (test + (exam * 2.0)) / 3.0`
     - If exam only: `average = exam`
     - If none present: `average = null` (Displayed as "--" or "غير مرصود")

2. **Attendance Entity (`AttendanceRecord.kt`)**:
   - Enums: `PRESENT`, `ABSENT`, `LATE`, `EXCUSED`
   - Individual Attendance Rate:
     $$\text{Rate} = \frac{N_{\text{PRESENT}} + N_{\text{LATE}}}{N_{\text{PRESENT}} + N_{\text{LATE}} + N_{\text{ABSENT}}} \times 100$$
   - Total absences counter increments only for `ABSENT` status.

3. **Student Entity (`Student.kt`)**:
   - Dynamically calculates `computedTermAverage` from actual `GradeRecord` relations instead of static placeholder numbers.

---

## 5. QA Audit Matrix & Execution Plan

| # | Audit Feature & Requirement | Expected Result | Verification Strategy |
|---|-----------------------------|-----------------|------------------------|
| 1 | **Non-functional / Decorative Buttons** | Every button (add classroom, add student, save grade, date pickers, language toggle, sign out) triggers a valid action or dialog. | Code inspection across all 11 screens + Compose interaction test. |
| 2 | **Zero Fabricated Production Statistics** | Empty database shows exact counts of 0 classrooms, 0 students, 0% averages. No fake data in authenticated mode. | Robolectric test initializing empty repository. |
| 3 | **Classroom & Student Counts Accuracy** | `totalClassroomsCount == classrooms.size`, `totalStudentsCount == students.size`. | Automated unit test asserting exact list size matching. |
| 4 | **Male & Female Student Totals** | `maleStudentsCount + femaleStudentsCount == totalStudentsCount`. | Automated test with varying gender mixes. |
| 5 | **Attendance Percentage Denominator** | Rate calculation divides only by recorded valid sessions; handles 0 recorded sessions safely without `NaN` or `ArithmeticException`. | Unit test simulating 0 sessions, 5 sessions, and mixed excused absences. |
| 6 | **Missing Grades Not Converted to Zero** | A student missing an exam receives an average based on completed tests, or `null`. An unentered exam does NOT set average to 0.0. | Arithmetic unit test with null grades. |
| 7 | **Weighted Average Mathematical Accuracy** | Algerian formula `(((devoirAvg) + exam*2)/3)` verified against manually calculated floating point values to 2 decimal places. | Automated test comparing known inputs to expected formula results. |
| 8 | **Charts Reflect Underlying Data** | Statistics screen distribution bars match exact counts of student mentions (ممتاز, جيد, etc.). | Unit test checking distribution histogram computation. |
| 9 | **Add / Edit / Delete Synchronous Propagation** | Adding a student immediately updates Classroom count, Student count, Male/Female totals, and Dashboard. | Reactive Flow test in `OstadiViewModelTest`. |
| 10 | **Real & Secure Authentication** | Passwords never saved in Firestore; Firebase Auth UID strictly used; unauthenticated users cannot read private teacher data. | Static rule analysis + repository authentication check tests. |
| 11 | **Cross-Teacher Isolation (Multi-Tenant)** | Teacher A (`uid_1`) cannot read or write to Teacher B (`uid_2`). Firestore rules enforce strict equality. | Automated Firestore rules unit test simulating dual-user context. |
| 12 | **RTL Rendering & Bilingual Support** | Arabic layout is completely mirrored (RTL); French toggle switches to LTR; all user-facing strings are translated. | Resource & Composable layout direction validation. |
| 13 | **Empty Database Stability** | Empty state does not crash on divisions by zero or empty collection reductions. | Robolectric test with clean empty Firestore state. |
| 14 | **Network Resilience** | Offline Firestore caching handles network disconnects gracefully; error notifications are displayed via Snackbar. | Simulated repository error throwing test. |
| 15 | **No Overlapping Elements** | Safe insets (`safeDrawing`, `navigationBarsPadding`) applied to top bar, bottom bar, and dialogs. | Composable layout structure inspection. |
| 16 | **Small Device Readability** | Scrollable columns (`verticalScroll`) on all forms and dialogs to prevent keyboard clipping. | Code review of all screen composable containers. |
| 17 | **Navigation Integrity** | All 11 navigation routes resolve correctly; system BackHandler pops sub-screens to Dashboard/Welcome properly. | Navigation state transition tests. |

---

## 6. Execution Steps (Post-Approval)

1. **Step 1: Arithmetic & Model Refactoring**:
   - Refactor `GradeRecord.kt` to support nullable grade components (`Double?`).
   - Implement the adaptive Algerian weighting algorithm without zero-penalty distortion.
   - Refactor `Student.kt` and `OstadiUiState.kt` to compute dynamic averages rather than static fields.
   - Fix attendance denominator math to handle zero sessions and excused absences cleanly.

2. **Step 2: Dual-Mode Isolation & Empty Database Hardening**:
   - Ensure `OstadiViewModel` cleanly decouples Demo Mode from Authenticated Firestore mode.
   - Harden all statistical reductions (`generalAverageScore`, `successRatePercentage`) with guards against empty collections.

3. **Step 3: Comprehensive Robolectric & Unit Test Suite**:
   - Create `OstadiCalculationAndIsolationTest.kt` implementing the two-teacher isolation test, 3 classrooms, male/female students, varying attendance sessions, and nullable grade scenarios.
   - Run `gradle :app:testDebugUnitTest` to execute all tests and obtain empirical pass/fail evidence.

4. **Step 4: UI & Navigation Polish**:
   - Verify every button has an active lambda handler and state trigger.
   - Ensure all dialogs have proper IME and scroll padding for small screen compatibility.
   - Verify RTL/LTR string parity between Arabic and French.

5. **Step 5: Full Audit Report Generation**:
   - Deliver the final QA audit table with empirical test results, actual values, and pass confirmations.
