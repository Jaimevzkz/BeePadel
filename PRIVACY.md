# Privacy Policy for BeePadel

_Last updated: 2026-08-09_

BeePadel is an open source pádel match tracker for Android and Wear OS. This policy explains what data the app collects, how it's used, and your choices — including data read via **Health Connect / body sensors** (heart rate).

## Summary

- BeePadel stores your match data (scores, dates, duration, heart rate stats) **locally on your device**.
- BeePadel does **not** have a backend server, does **not** show ads, and does **not** use any analytics or tracking SDKs.
- Heart rate data is read from your Wear OS watch **only during an active match**, to show you your live BPM and to save your average/max heart rate with that match, on your device.
- Data only leaves your device if **you** choose to export it, or if **you** choose to connect and sync with Strava.

## What data BeePadel collects

### Match data
Scores, match duration, and dates you create while using the app. Stored locally in the app's database on your device.

### Heart rate (Health Connect / body sensor data)
If you use the Wear OS companion app during a match, BeePadel reads your heart rate (BPM) from your watch's on-body sensor via the Android Health Services / Health Connect permission (`android.permission.health.READ_HEART_RATE`, or `BODY_SENSORS` on older Android versions).

This data is used exclusively to:
- Display your live heart rate on the watch screen while you are playing a match.
- Calculate and store the average and maximum heart rate for that match, shown afterward in your own match history.

Heart rate data is **not**:
- Sent to any BeePadel server (BeePadel has no backend).
- Used for advertising, profiling, or analytics.
- Sold or shared with any third party — except Strava, and only if you explicitly enable the optional Strava sync described below.

## Your choices

- **Import/Export**: you can export your match history (including heart rate stats) to a local JSON file, and import it back at any time. This is entirely under your control.
- **Strava sync (optional, off by default)**: if you choose to connect your account to Strava, your completed matches — including heart rate stats — are uploaded to your own Strava account so they appear as activities there. You can disconnect this at any time in Settings. See [Strava's own privacy policy](https://www.strava.com/legal/privacy) for how Strava handles that data once synced.
- **Deleting your data**: uninstalling the app removes all locally stored match and heart rate data. You may also delete individual matches from within the app.

## Third-party services

- **Strava** — only if you opt in to "Connect with Strava" in Settings.

BeePadel does not use any advertising, analytics, or crash-reporting SDKs.

## Permissions

| Permission | Why BeePadel needs it |
|---|---|
| `BODY_SENSORS` / `android.permission.health.READ_HEART_RATE` | Read live heart rate from your Wear OS watch during a match, and compute your average/max heart rate for that match. |
| Notifications | Show match-tracking status while a match is in progress. |
| Internet | Sync with Strava (only if you opt in), and check for app updates. |

## Open source

BeePadel is open source. You can review exactly how your data is handled in the source code at [github.com/Jaimevzkz/BeePadel](https://github.com/Jaimevzkz/BeePadel).

## Contact

Questions about this policy or your data: **jaimevazquezmartin23@gmail.com**