package com.bham.fsd.assignments.jabberserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;

public class ClientConnection implements Runnable{
    private Socket socketClientConnection;
    //database
    private JabberDatabase jabberDatabase;
    //
    private JabberServer jabberServer = new JabberServer();


    public ClientConnection(Socket socket, JabberDatabase jabberDatabase)
    {
        this.socketClientConnection = socket;
        this.jabberDatabase = jabberDatabase;
    }

    UserInfo userInfo = new UserInfo();

    private static class UserInfo
    {
        private String userNameStored;

        public void setUserNameStored(String theName)
        {
            this.userNameStored = theName;
        }

        public String getUserNameStored()
        {
            return userNameStored;
        }
    }


    public JabberMessage signInRequest(String userName) {

        StringBuilder sb = new StringBuilder();
        sb.append(userName);
        sb.delete(0,7);

        userInfo.setUserNameStored(sb.toString());


        int result = jabberDatabase.getUserID(sb.toString());
        JabberMessage jabberMessage;
        if(result == -1)
        {
            jabberMessage = new JabberMessage("unknown-user");


        }
        else
        {
            jabberMessage = new JabberMessage("signedin");


        }
        return jabberMessage;

    }



    public Boolean SignOutRequest(Boolean booleanTruth)
    {
        if(!booleanTruth)
        {
            return false;
        }
        return true;
    }

    public JabberMessage RegisterationRequest(String userName)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(userName);
        sb.delete(0,8);

        userInfo.setUserNameStored(sb.toString());


        String email = sb + "@email.com";
        jabberDatabase.addUser(sb.toString(), email);
        JabberMessage message = new JabberMessage("signedin");
        return message;
    }


    public JabberMessage TimeLineRequest(String timelineUsername)
    {
        ArrayList<ArrayList<String>> result = new ArrayList<>();

        result = jabberDatabase.getTimelineOfUserEx(userInfo.getUserNameStored());
        if(result == null)
        {
            result = new ArrayList<>();
        }
        JabberMessage message = new JabberMessage(timelineUsername,result);



        return message;

    }

    public JabberMessage postAJab(String postAJab)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(postAJab);
        sb.delete(0,5);



        jabberDatabase.addJab(userInfo.getUserNameStored(), sb.toString());
        JabberMessage message = new JabberMessage("posted");
        return message;
    }

    public JabberMessage likeAJab(String jabid)
    {
        StringBuffer sb = new StringBuffer();
        sb.append(jabid);
        sb.delete(0,5);

        int userA = jabberDatabase.getUserID(userInfo.getUserNameStored());
        jabberDatabase.addLike(userA, Integer.parseInt(sb.toString()));
        JabberMessage message = new JabberMessage("posted");
        return message;

    }

    public JabberMessage followAUser(String username)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(username);
        sb.delete(0,7);

        int userA = jabberDatabase.getUserID(userInfo.getUserNameStored());

        jabberDatabase.addFollower(userA, sb.toString());
        JabberMessage message = new JabberMessage("posted");
        return message;
    }

    public JabberMessage whoToFollow(String username)
    {
        ArrayList<ArrayList<String>> resultArray;

        int userA = jabberDatabase.getUserID(userInfo.getUserNameStored());
        resultArray = jabberDatabase.getUsersNotFollowed(userA);
        JabberMessage message = new JabberMessage(username, resultArray);


        return message;
    }


    @Override
    public void run() {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socketClientConnection.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socketClientConnection.getInputStream());
            while (SignOutRequest(true)) {
                //TODO i need to make sure the server accepts the client
                //TODO implement a method that check the client has not requested a stop/ signout this method shoudl stay alert until the user signs out

                System.out.println("Client Connection!!!");

                //outputStream from jabberMessage
                //this needs to accessed by the jabberServer
                JabberMessage request = (JabberMessage)objectInputStream.readObject();
                JabberMessage response = null;

                if(request.getMessage().contains("signin"))
                {
                    response = signInRequest(request.getMessage());

                }
                else if(request.getMessage().contains("register"))
                {
                    response = RegisterationRequest(request.getMessage());
                }
                else if(request.getMessage().contains("post"))
                {
                    response = postAJab(request.getMessage());

                }
                else if(request.getMessage().contains("like"))
                {
                    response = likeAJab(request.getMessage());
                }
                else if(request.getMessage().contains("follow"))
                {
                    response = followAUser(request.getMessage());
                }


                switch (request.getMessage())
                {
                    case "signout":
                        SignOutRequest(false);
                        response = null;
                        break;
                    case "timeline":
                        response = TimeLineRequest(request.getMessage());
                        break;
                    case "users":
                        response = whoToFollow(request.getMessage());
                        break;
                }

                objectOutputStream.writeObject(response);

            }
        }catch (IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }

    }


}
