# GameNative Forwarder

A simple forwarder application that enables any frontend to send an intent using only the filename of the frontend sync file exported from [GameNative](https://github.com/utkarshdalal/GameNative).

## Usage
1. Build and install the app onto your device
2. Grant all files access permission to GameNative Forwarder
3. Set up your frontend to send an intent with these parameters:
  * Component: `com.farmerbb.gamenative/.MainActivity`
  * Action: `com.farmerbb.gamenative.LAUNCH_GAME`
  * Data: URI or path to the GameNative frontend sync file
4. Launch the game using your frontend. GameNative Forwarder then takes care of launching the actual GameNative application.

## AI Disclosure
Portions of this app were made using generative AI. Android Studio's "Create with AI" feature in the New Project dialog was used to generate a basic skeleton project with an initial draft of the intent forwarding feature. I then made significant manual modifications to the code until it worked as I desired. Every single line was manually reviewed before being committed to this repository, and a lot of unnecessary cruft was manually removed beforehand.

I strongly disagree with the concept of "vibe-coding" or "AI-native" software development. However, I do believe that agentic AI can be useful in specific cases, purely as a tool to augment certain aspects of the developer's workflow while still allowing the developer to maintain full control over the process of writing and architecting their software. I also strongly believe in maximum transparency when AI tools are being used, hence the existence of this disclosure.
