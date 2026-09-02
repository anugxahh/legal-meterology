# LMO Verify SIH — Gmail + Profile

Customer and LMO login now contain Username, Gmail Address, and Password.
The entered Username and Gmail are stored in the current demo session and displayed in the corresponding Profile page.
The existing Customer application-to-profile sync and navigation behavior are preserved.


Top-bar Change Role button removed. Authenticated screens retain Logout; sign-in retains the Back button inside the form.


Logout fix: the authenticated Logout button is now wired to a working logout action that clears session state and returns to role selection.
