import SwiftUI
import ComposeApp

@main
struct iOSApp: App {

    init() {
        MainControllerKt.initialise()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}