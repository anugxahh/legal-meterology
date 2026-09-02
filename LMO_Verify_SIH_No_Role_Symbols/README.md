# LMO Verify — User Entity Fields + User ID

The Customer application now includes input space for the complete User entity:

- User ID
- Name
- Email Address
- Phone Number
- Password
- Business / Establishment Name
- Address

The User ID is now visible and editable in the frontend so it can be mapped to the backend field `id` as requested.

All previous features are preserved:
Customer/LMO/Admin login, Back button, white password eye, green secure portal light, application form, status/certification flow, certificate/QR placeholders, LMO location gate and locked Verification sidebar, sequential LMO verification, evidence upload, single Submit Verification, Admin pages, and kilometre location display.

Backend architecture:
Frontend -> Spring Boot -> SQL


Role cards now use professional text labels: CUSTOMER, LMO, and ADMIN.
