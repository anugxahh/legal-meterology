# LMO Verify — Profile Sync Fixed

Fixed Customer New Application -> Profile synchronization.

When a Customer submits the New Application form in prototype mode:
- User ID, name, email, phone, businessName, and address are captured.
- The Customer Profile immediately shows the submitted values.
- Application details are also stored in the frontend data state.
- The password is never displayed in the profile.

When `API.DEMO_MODE=false`, the same captured payload is posted to the configured Spring Boot application endpoint, and profile/application data can be reloaded from the backend.

LMO/Admin profiles remain backend-driven and start from 0/empty until Spring Boot returns their records.

All previous features, including the LMO location gate and kilometre display, are preserved.


Final role selection uses professional text labels CUSTOMER, LMO, and ADMIN. No role symbols are used.
