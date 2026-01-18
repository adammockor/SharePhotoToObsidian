# SharePhotoToObsidian

Android app that lets you share photos (e.g. from Google Photos) directly into an Obsidian vault folder.

## What it does
- Appears in Android Share menu
- Copies shared photos into a user-selected folder
- Renames files to include original filename + capture datetime
- Works with Obsidian + sync tools (DriveSync, Syncthing, etc.)

## Setup
1. Install the app
2. Open it once and pick your Obsidian attachments folder
3. Share photos from Google Photos → **SharePhotoToObsidian**

## Filename format

`_yyyy-MM-dd_HH-mm-ss.jpg`

## Notes
- Uses Android Storage Access Framework (no hardcoded paths)
- EXIF capture time is used when available
- Google Photos sharing may provide a copy, not the original file

## License
MIT