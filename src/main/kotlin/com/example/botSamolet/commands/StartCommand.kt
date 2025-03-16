package com.example.botSamolet.commands

import com.example.botSamolet.models.CommandName
import com.example.botSamolet.repositories.UsersRepository
import com.example.botSamolet.services.HouseService
import org.springframework.stereotype.Component
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender

@Component
class StartCommand( private val houseService: HouseService,
                   private val userRep: UsersRepository,
                   private val menu: MenuCommand) : BotCommand(CommandName.START.text, "") {
    override fun execute(absSender: AbsSender, user: User, chat: Chat, arguments: Array<out String>) {
        val new = com.example.botSamolet.models.Users(user.id, user.firstName, user.isBot, user.lastName,
            user.userName, user.languageCode, user.canJoinGroups,
            user.canReadAllGroupMessages, user.supportInlineQueries, user.isPremium, user.addedToAttachmentMenu )
        //userRep.save(new).subscribe()
        houseService.saveUser(new)
        menu.execute(absSender = absSender, user = user, chat = chat, arguments = arguments)
    }
}