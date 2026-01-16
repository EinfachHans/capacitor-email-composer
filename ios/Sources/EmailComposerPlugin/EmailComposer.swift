import Foundation
import Capacitor
import MessageUI
import MobileCoreServices

extension String: Error {}

@objc public class EmailComposer: NSObject {
    func canSendMail() -> Bool {
        return MFMailComposeViewController.canSendMail()
    }

    func getMailComposerFromCall(_ call: CAPPluginCall, delegateTo: MFMailComposeViewControllerDelegate) throws -> MFMailComposeViewController {
        let draft = MFMailComposeViewController()

        // Subject
        draft.setSubject(call.getString("subject", ""))

        // Body
        draft.setMessageBody(call.getString("body", ""), isHTML: call.getBool("isHtml", false))

        // TO
        draft.setToRecipients(call.getArray("to", String.self))

        // CC
        draft.setCcRecipients(call.getArray("cc", String.self))

        // BCC
        draft.setBccRecipients(call.getArray("bcc", String.self))

        try self.setAttachments(draft: draft, call)

        draft.mailComposeDelegate = delegateTo
        return draft
    }

    private func setAttachments(draft: MFMailComposeViewController, _ call: CAPPluginCall) throws {
        let attachments = call.getArray("attachments", JSObject.self) ?? []

        for attachment in attachments {
            let path = attachment["path"] as! String
            let type = attachment["type"] as! String
            let name = attachment["name"] as? String

            let data = try self.getDataForAttachmentPath(path: path, type: type)

            if data == nil {
                continue
            }

            let baseName: String
            let pathExtension: String
            if name != nil {
                baseName = name!
                pathExtension = (name! as NSString).pathExtension
            } else {
                if type == "base64" {
                    throw "A base64 attachment needs a name!"
                } else {
                    baseName = (path as NSString).lastPathComponent
                    pathExtension = (path as NSString).pathExtension
                }
            }

            draft.addAttachmentData(data!, mimeType: self.getMimeTypeFromFileExtension(pathExtension: pathExtension), fileName: baseName)
        }
    }

    private func getDataForAttachmentPath(path: String, type: String) throws -> Data? {
        switch type {
        case "absolute":
            return try self.getDataFromAbsolutePath(path: path)
        case "resource":
            return try self.getDataFromResourcePath(path: path)
        case "asset":
            return try self.getDataFromAssetPath(path: path)
        case "base64":
            return self.getDataFromBase64(base64: path)
        default:
            throw "Unknown Attachment Type"
        }
    }

    private func getDataFromAbsolutePath(path: String) throws -> Data? {
        let fileManager = FileManager.default
        if !fileManager.fileExists(atPath: path) {
            throw "File does not exist at absolute path"
        }

        return fileManager.contents(atPath: path)
    }

    private func getDataFromResourcePath(path: String) throws -> Data? {
        let imgName = NSString(string: (path as NSString).deletingPathExtension).pathComponents.last

        let image = UIImage(named: imgName!)
        if image == nil {
            throw "File does not exist at resources"
        }

        return image?.pngData()
    }

    private func getDataFromAssetPath(path: String) throws -> Data? {
        let bundlePath = Bundle.main.bundlePath
        let absPath = bundlePath + "/public/assets" + path

        return try self.getDataFromAbsolutePath(path: absPath)
    }

    private func getDataFromBase64(base64: String) -> Data? {
        return Data.init(base64Encoded: base64, options: NSData.Base64DecodingOptions.ignoreUnknownCharacters)
    }

    private func getMimeTypeFromFileExtension(pathExtension: String?) -> String {
        if pathExtension == nil {
            return "application/octet-stream"
        }

        if let uti = UTTypeCreatePreferredIdentifierForTag(kUTTagClassFilenameExtension, pathExtension! as NSString, nil)?.takeRetainedValue() {
            if let mimetype = UTTypeCopyPreferredTagWithClass(uti, kUTTagClassMIMEType)?.takeRetainedValue() {
                return mimetype as String
            }
        }
        return "application/octet-stream"
    }
}
