package com.example.botSamolet.handlers

import com.example.botSamolet.commands.*
import com.example.botSamolet.models.HandlerName
import com.example.botSamolet.repositories.FavoritesRepository
import com.example.botSamolet.repositories.HouseRepository
import com.example.botSamolet.services.HouseService
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.InputFile
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import org.telegram.telegrambots.meta.bots.AbsSender
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import java.text.SimpleDateFormat
import java.time.ZoneOffset
import java.util.*

@Component
class AnswerHandler(
    private val houseService: HouseService,
    private val housesRep: HouseRepository,
    private val favoritesRep: FavoritesRepository,
    private val menu: MenuCommand,
    private val type: TypeCommand,
    private val get: GetCommand
) : CallbackHandler {

    override val name: HandlerName = HandlerName.BOT_ANSWER

    override fun processCallbackData(
        absSender: AbsSender,
        callbackQuery: CallbackQuery,
        arguments: List<String>
    ) {

        val chatId = callbackQuery.message.chatId.toString()
        val test = callbackQuery.message.toString()
        val res = test.split("messageId=")
        val next = res[1].split(",")
        val messageId = next[0]

        val text = callbackQuery.message.toString().split("text=")[1].split(",")[0]

        if (arguments.isNotEmpty()) {

            if (arguments.first() == "menu") {
                absSender.execute(DeleteMessage(chatId, messageId.toInt()))
                menu.execute(
                    absSender = absSender, user = callbackQuery.from, chat = callbackQuery.message.chat,
                    arguments = arguments.toTypedArray()
                )
                return
            }

            if (arguments.first().contains("favorite")) {
                val fav = favoritesRep.select(callbackQuery.from.id, arguments.first().split('/')[1].toInt())
                    .collectList().block()
                if (fav?.isNotEmpty() == true) {
                    favoritesRep.delete(callbackQuery.from.id, arguments.first().split('/')[1].toInt()).subscribe()
                    updateInlineButtons(absSender, chatId.toLong(), messageId.toInt(), arguments.first().split('/')[1].toInt()
                        , "⭐Добавить в избранное")
                    return
                }

                //val callback = HandlerName.BOT_ANSWER.text

                favoritesRep.insert(callbackQuery.from.id, arguments.first().split('/')[1].toInt()).subscribe()
                updateInlineButtons(absSender, chatId.toLong(), messageId.toInt(), arguments.first().split('/')[1].toInt()
                    , "⭐Убрать из избранного")
                return
            }

            if (arguments.first() == "myfav") {
                val fav = favoritesRep.selectall(callbackQuery.from.id).collectList().block()
                if (fav?.isNotEmpty() == true) {
                    absSender.execute(DeleteMessage(chatId, messageId.toInt()))
                    val newarg = mutableListOf("FAV", "*")
                    fav.forEach {
                        newarg += it.id.toString()
                    }

                    get.execute(
                        absSender = absSender, user = callbackQuery.from, chat = callbackQuery.message.chat,
                        arguments = newarg.toTypedArray()
                    )
                }
                return
            }

            if (arguments.first().contains("ДМИ")
                || arguments.first().contains("ИСТ")
                || arguments.first().contains("ПУШ")
                || arguments.first().contains("ЭММ")
            ) {
                absSender.execute(DeleteMessage(chatId, messageId.toInt()))

                val argumentsList = arguments.first().split('/').toTypedArray()

                if (argumentsList[2] == "%"){
                    type.execute(
                        absSender = absSender, user = callbackQuery.from, chat = callbackQuery.message.chat,
                        arguments = argumentsList
                    )
                } else {
                    get.execute(
                        absSender = absSender, user = callbackQuery.from, chat = callbackQuery.message.chat,
                        arguments = argumentsList
                    )
                }

                return
            }

            //выбран дом
            val house = houseService.fetchHouse(arguments.first().toInt())
            val houseHistory = houseService.fetchHouses("","","")
            //val house = housesRep.getHouse(arguments.first().toInt()).collectList().block()

            var answer = "${house.article}\n" +
                    "${house.type} (${house.land_area} соток, ${house.area} кв.м.)\n" +
                    "${house.technology}\n" +
                    "Цена земли ${house.land_price} рублей\n" +
                    "Дата сдачи ${house.completion_date}\n" +
                    "Цена ${house.price} рублей\n\n"

            var price = 0
            var direct = ""
            answer += "История цен:\n"
            houseHistory.forEach {
                if (price < it.price) {
                    direct = "⬆\uFE0F"
                } else if (price > it.price) {
                    direct = "⬇\uFE0F"
                }
                val dateFormated =
                    SimpleDateFormat("dd/MM/yyyy").format(Date.from(it.date!!.toInstant(ZoneOffset.UTC)))
                if (it.status == 3) {
                    price = it.price
                    answer += "\uD83D\uDCB0 $dateFormated продан по цене ${it.price} рублей\n"
                } else {
                    price = it.price
                    answer += "$direct $dateFormated цена ${it.price} рублей\n"
                }
            }

            answer += "\n${house.url}"

            absSender.execute(DeleteMessage(chatId, messageId.toInt()))

            val sendPhoto = SendPhoto()
            sendPhoto.chatId = chatId
            sendPhoto.caption = answer
            sendPhoto.setPhoto(InputFile(house.image))
            val sentMessageId = absSender.execute(sendPhoto).messageId

            val fav = favoritesRep.select(callbackQuery.from.id, house.id)
                .collectList().block()
            if (fav?.isNotEmpty() == true) {
                updateInlineButtons(absSender, chatId.toLong(), sentMessageId, arguments.first().toInt()
                    , "⭐Убрать из избранного")
            } else {
                updateInlineButtons(absSender, chatId.toLong(), sentMessageId, arguments.first().toInt()
                    , "⭐Добавить в избранное")
            }
            return
        }
    }

    fun updateInlineButtons(absSender: AbsSender, chatId: Long, messageId: Int, id: Int, txt: String) {
        val callback = HandlerName.BOT_ANSWER.text

        val newButton1 = InlineKeyboardButton().apply {
            text = txt
            callbackData = "$callback|favorite/$id"
        }
        val newButton2 = InlineKeyboardButton().apply {
            text = "<< назад"
            callbackData = "$callback|menu"
        }

        var newKeyboard = listOf(listOf(newButton1))
        newKeyboard += listOf(listOf(newButton2))
        val newMarkup = InlineKeyboardMarkup().apply {
            keyboard = newKeyboard
        }

        val editMarkup = EditMessageReplyMarkup().apply {
            this.chatId = chatId.toString()
            this.messageId = messageId
            this.replyMarkup = newMarkup
        }

        try {
            absSender.execute(editMarkup) // Updating the message with new buttons
        } catch (e: TelegramApiException) {
            e.printStackTrace()
        }
    }
}