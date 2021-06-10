import Foundation
import Capacitor
import MessageUI

@objc public class EmailComposer: NSObject {
    func canSendMail() -> Bool {
        return MFMailComposeViewController.canSendMail();
    }
    
    func getMailComposerFromCall(_ call: CAPPluginCall, delegateTo: MFMailComposeViewControllerDelegate) -> MFMailComposeViewController {
        let draft = MFMailComposeViewController();
        
        // Subject
        draft.setSubject(call.getString("subject", ""));
        
        // Body
        draft.setMessageBody(call.getString("body", ""), isHTML: false);
        
        // TO
        draft.setToRecipients(call.getArray("to", String.self));
        
        // CC
        draft.setCcRecipients(call.getArray("cc", String.self));
        
        // BCC
        draft.setBccRecipients(call.getArray("bcc", String.self));
        
        draft.mailComposeDelegate = delegateTo;
        return draft;
    }
}
