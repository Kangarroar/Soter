package cl.sandoval.soter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class chat extends AppCompatActivity {

    private RecyclerView chatRecyclerView;
    private EditText messageInput;
    private Button sendButton;

    private FirebaseFirestore db;
    private ChatAdapter chatAdapter;
    private ArrayList<Message> messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        messageInput = findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.sendButton);

        db = FirebaseFirestore.getInstance();
        messageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(messageList);

        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(chatAdapter);

        loadMessages();

        sendButton.setOnClickListener(v -> sendMessage());
    }

    private void loadMessages() {
        db.collection("messages")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .limit(100)
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null || snapshots == null) return;

                    for (DocumentChange dc : snapshots.getDocumentChanges()) {
                        if (dc.getType() == DocumentChange.Type.ADDED) {
                            Message message = dc.getDocument().toObject(Message.class);
                            messageList.add(message);
                            chatAdapter.notifyItemInserted(messageList.size() - 1);
                            chatRecyclerView.scrollToPosition(messageList.size() - 1);
                        }
                    }
                });
    }

    private void sendMessage() {
        String text = messageInput.getText().toString().trim();
        if (TextUtils.isEmpty(text)) return;

        // Obtener el usuario actual
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String sender = (user != null) ? user.getDisplayName() : "null";  // Si el usuario es null, asignar "null"

        // Crear el mensaje
        Map<String, Object> message = new HashMap<>();
        message.put("text", text);
        message.put("sender", sender);
        message.put("timestamp", com.google.firebase.firestore.FieldValue.serverTimestamp());  // Obtener la fecha/hora del servidor

        // Guardar el mensaje en Firestore
        db.collection("messages").add(message)
                .addOnSuccessListener(documentReference -> {
                    // Si el mensaje se guardó correctamente, limpiar el campo de entrada
                    messageInput.setText("");
                })
                .addOnFailureListener(e -> {
                    // Si ocurre un error, mostrar el error
                    e.printStackTrace();
                });
    }

}

