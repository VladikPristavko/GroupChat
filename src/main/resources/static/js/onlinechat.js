$(function(){
    //global variables
    let lastMessageTime = ''; // for upload only messages posted after last given message
    let enterChatTime = '';   // for print time that get from 2 requests(/auth and /init)
    let usersCount = 0;       // for single upload all users, if count not change
    //execute chat
    let initApplication = function(name){
        // init all chat windows and button
        $('.login').css({display: 'none'});
        $('.lists').css({display: 'flex'});
        $('.controls').css({display: 'flex'});
        $('.user-label').css({display: 'block'});
        $('.new-message').val('');
        $('.new-message').focus();
        $('.send-message').text('Send');
        //print in Label Username and enter time
        $('.user-label').text('Username: <' + name + '>. Enter Chat in: <' + enterChatTime + '>.');
        //initial string in chatbox
        $('.messages-list').append($('<i>No new messages from users</i>'));
        //new message post handler(button Send)
        $('.send-message').on('click', function(){
            let text = $('.new-message').val();
            $.post('/message', {text : text, name : name}, function(response){
                switch(response.result){
                    case 'notAuthorized' :
                        alert('You are not authorized!');
                        break;
                    case 'empty' :
                        alert('Message is empty or null!');
                        break;
                    case 'ok' :
                        $('.new-message').val('');
                        break;
                };
            });
            $('.new-message').focus();
        });
        //exit handler(button Exit)
        $('.exit-chat').on('click', function(){
            //POST request set SessionId to "empty"
            $.post('/exit', {name : name}, function(response){
                if(response.result == 'ok'){
                    //hide all windows
                    $('.lists').css({display: 'none'});
                    $('.controls').css({display: 'none'});
                    $('.user-label').css({display: 'none'});
                    //stop GET requests for Messages and Users
                    clearInterval(interval);
                    //call registration window
                    init();
                }
                if(response.result == 'noUser'){
                    alert('Username out of database!')
                }
            });
        });
        //each 1 sec update messages and users
        let interval = setInterval(function(){
            updateMessages(lastMessageTime);
            updateUsers();
            }, 1000
        );
    };
    //enter or register user
    $.get('/init', {}, function(response){
        if(response.result === 'new'){
            init();
            return;
        }
        if(response.result === 'ok'){
            enterChatTime = response.enter;
            initApplication(response.name);
        }
    });
    let registerUser = function(name, pwd){
        $.post('/auth', {name : name, password : pwd}, function(response){
            switch(response.result){
                case 'wrongPWD' :
                    alert('User already defined! Wrong password!');
                    break;
                case 'empty' :
                    alert('Name is empty or null!');
                    break;
                case 'ok' :
                    enterChatTime = response.enter;
                    initApplication(response.name);
                    break;
            };
        });
    };
    //input form handler
    let init = function(){
        $('.login').css({display: 'inline-block'});
        $('.enter').on('click', function(){
            let name = $('.log').val();
            let pwd = $('.pass').val();
            registerUser(name, pwd);
        });
        $('.exit').on('click', function(){
            $('.login').css({display: 'none'});
        });
    };
    //get and print message
    let getMessageElement = function(message){
        let item = $('<div class="message-item"></div>');
        let header = $('<div class="message-header"></div>');
        let text = $('<div class="message-text">' + message.text + '</div>');
        header.append($('<div class="datetime">' + message.datetime + '</div>'));
        header.append($('<div class="username">' + message.username + '</div>'));
        item.append(header, text);
        return item;
    };
    let updateMessages = function(time){
        $.get('/message', {time : time}, function(response){
            if(response.length == 0){
                return;
            }
            lastMessageTime = response[response.length - 1].datetime;
            for(i in response){
                let element = getMessageElement(response[i]);
                $('.messages-list').append(element);
            }
            lastMessageScroll('smooth');
        });
    };
    let lastMessageScroll = function(scrollBehavior){
        //Scroll chat window on the last message
        let messageItems = document.querySelectorAll('.message-item');
            if (!messageItems){
                return;
            };
        messageItems[messageItems.length - 1].scrollIntoView({
            behavior: scrollBehavior || 'auto',
            block: 'end',
        });
    };
    //get and update users
    let updateUsers = function(){
        $.get('/users-count', {}, function(response){
            if(usersCount != response){
                loadUsers();
            };
        });
    };
    let loadUsers = function(){
        $.get('/users', {}, function(response){
            if(response.length == 0){
                return;
            }
            usersCount = response.length;
            $('.users-list').html('');
            $('.users-list').append($('<div class="allUsers"></div>'));
            $('.allUsers').text('All Users(' + usersCount + ' total):');
            for(i in response){
                $('.users-list').append($('<div class="user">' + response[i] + '</div>'));
            }
        });
    };
});