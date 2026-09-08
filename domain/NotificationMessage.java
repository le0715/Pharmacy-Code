package pharmacy.domain;

import java.time.LocalDateTime;


public class NotificationMessage {

    private final String notificationId;
    private final String recipientEmailAddress;
    private final String messageText;
    private final LocalDateTime dispatchTimestamp;
    private boolean isReadByRecipient;

    public NotificationMessage(String notificationId, String recipientEmailAddress, String messageText) {
        this.notificationId = notificationId;
        this.recipientEmailAddress = recipientEmailAddress;
        this.messageText = messageText;
        this.dispatchTimestamp = LocalDateTime.now();
        this.isReadByRecipient = false;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public String getRecipientEmailAddress() {
        return recipientEmailAddress;
    }

    public String getMessageText() {
        return messageText;
    }

    public LocalDateTime getDispatchTimestamp() {
        return dispatchTimestamp;
    }

    public boolean isReadByRecipient() {
        return isReadByRecipient;
    }

    public void markAsRead() {
        this.isReadByRecipient = true;
    }
}
