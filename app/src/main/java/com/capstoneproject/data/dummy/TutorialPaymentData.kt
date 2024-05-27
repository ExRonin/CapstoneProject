package com.capstoneproject.data.dummy

import com.capstoneproject.data.model.TutorialPayment

object TutorialPaymentData {

    private val tutorialNames = arrayOf(
        "Transfer Melalui ATM",
        "Transfer Melalui Livin by Mandiri",
        "Transfer Melalui Internet Banking"
    )

    private val tutorialDesc = arrayOf(
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
    )

    private val tutorialExpands = arrayOf(
        false,
        false,
        false
    )

    val listData: ArrayList<TutorialPayment>
        get() {
            val list = arrayListOf<TutorialPayment>()
            for (position in tutorialNames.indices) {
                val tutorialPayment = TutorialPayment()
                tutorialPayment.name = tutorialNames[position]
                tutorialPayment.description = tutorialDesc[position]
                tutorialPayment.expand = tutorialExpands[position]
                list.add(tutorialPayment)
            }
            return list
        }
}