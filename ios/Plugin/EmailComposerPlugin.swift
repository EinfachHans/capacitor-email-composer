import Foundation
import Capacitor
import MessageUI

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(EmailComposerPlugin)
public class EmailComposerPlugin: CAPPlugin, MFMailComposeViewControllerDelegate {
    private let implementation = EmailComposer()

    private var savedOpenCall: CAPPluginCall?

    @objc func hasAccount(_ call: CAPPluginCall) {
        call.resolve(["hasAccount": self.implementation.canSendMail()])
    }

    @objc func open(_ call: CAPPluginCall) {
        DispatchQueue.main.async {
            do {
                self.savedOpenCall = call
                let draft = try self.implementation.getMailComposerFromCall(call, delegateTo: self)
                self.bridge?.viewController?.present(draft, animated: true, completion: nil)
            } catch {
                call.reject(error as! String)
            }
        }
    }

    public func mailComposeController(_ controller: MFMailComposeViewController, didFinishWith result: MFMailComposeResult, error: Error?) {
        controller.dismiss(animated: true, completion: nil)
        if self.savedOpenCall != nil {
            self.savedOpenCall?.resolve()
        }
    }
}
