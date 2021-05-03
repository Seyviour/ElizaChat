package com.zypus.eliza

import com.zypus.eliza.dsl.Goto
import com.zypus.eliza.dsl.Key
import com.zypus.eliza.dsl.Reassembly
import com.zypus.eliza.dsl.Script

/**
 * The main engine of ELIZA.
 *
 * @author Zypus
 *
 * @created 10.10.17
 */



class ElizaAndroid(var script: Script, val memory: MutableList<String> = arrayListOf()) {

    private var isRunning = true


    fun process(input: String): List<String>{

        val cleanInput = input
            .toLowerCase()
            .replace("[,!?]".toRegex(), ".")
            .replace("[^\\w\\d .]".toRegex(), "")
            .replace(" +".toRegex(), " ")
            .trim()

        val sentences = cleanInput
            .split(".")
            .map(String::trim)
            .filter(String::isNotBlank)

        return sentences
    }

    public fun answerSentences(input: List<String>): List<String> {

        val answerList: MutableList<String> = mutableListOf("")

        for (userInput in input) {

            answerList += answer(userInput)
        }

        return answerList
    }


    private fun answer(cleanInput: String): String {
        // check if the user wants to quit
        if (cleanInput in script.quits) {
            isRunning = false
            return "Bye, See you again soon."
        }

        val words = cleanInput.split(" ")

        // apply pre-processing transformations
        val preWords = words.map {
            script.preTransformations.getOrDefault(it, it)
        }

        val preparedInput = preWords.joinToString(separator = " ")

        // find keywords in the input and rank them according to their weight
        val keyStack: MutableList<Key> = script.keys.entries
            .filter { it.key in preWords }
            .sortedBy {
                it.value.index
            }
            .sortedByDescending {
                it.value.weight
            }.map(MutableMap.MutableEntry<String, Key>::value) as MutableList<Key>

        // if no keys where found try to recall something from memory or else use default sentences (key="xnone")
        if (keyStack.isEmpty() && memory.isNotEmpty()) {
            return answer(memory.removeAt(0))
        } else if (keyStack.isEmpty() && "xnone" in script.keys) {
            keyStack += script.keys["xnone"]!!
        }

        return if (keyStack.isNotEmpty()) {
            // fold is used to find the first matching decomposition
            val rawReply = keyStack.fold<Key, String?>(null) { reply, key ->

                // key processing function, to be used recursively if an "goto" action is triggered
                fun processKey(key: Key): String? {
                    return key.match(preparedInput)?.let { decomposition ->
                        if (decomposition.isMemorising) {
                            memory += preparedInput
                        }

                        val action = decomposition.nextAction()
                        when (action) {
                            is Goto ->
                                script.keys[action.target]?.let {
                                    processKey(it)
                                } ?: "<${key.name}(${decomposition.pattern}) Goto target '${action.target}' is missing>"
                            is Reassembly ->
                                action.assemble(decomposition.pattern, preparedInput, script.postTransformations)
                        }
                    }
                }

                reply ?: processKey(key)
            }
            rawReply ?: "I am at a loss for words."
        } else {
            "I am at a loss for words."
        }
    }
}

