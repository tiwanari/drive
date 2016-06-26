/*
 * Copyright (C) 2011-2015 PILE Project, Inc. <dev@pileproject.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pileproject.drive.programming.visual.block;

import android.content.Context;

import com.pileproject.drive.app.DriveApplication;
import com.pileproject.drive.programming.visual.block.repetition.RepetitionEndBlock;
import com.pileproject.drive.programming.visual.block.selection.SelectionEndBlock;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import trikita.log.Log;

/**
 * Factory that creates blocks
 *
 * @author <a href="mailto:tatsuyaw0c@gmail.com">Tatsuya Iwanari</a>
 * @version 1.0 18-June-2013
 */
public class BlockFactory {
    public static final int SEQUENCE = 0;
    public static final int REPETITION = 1;
    public static final int SELECTION = 2;
    public static final int UNDO = 3;
    public static final int LOAD = 4;

    /**
     * This class can't be created as an instance
     */
    private BlockFactory() {
    }

    /**
     * Return class by name
     *
     * @param className
     * @return Class
     */
    @SuppressWarnings("unchecked")
    private static <T> Class<T> getClassForName(String className) throws RuntimeException {
        try {
            return (Class<T>) Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Create blocks by reflecting
     *
     * @param blockName
     * @return
     */
    private static BlockBase create(String blockName) {

        // Get class
        Class<BlockBase> blockClass = null;
        try {
            blockClass = getClassForName(blockName);
        } catch (RuntimeException e) {
            Log.e(e.getMessage());
        }

        // Get constructor
        Class<?>[] types = {Context.class};
        Constructor<BlockBase> constructor;
        try {
            constructor = blockClass.getConstructor(types);
        } catch (SecurityException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        // Create a new instance
        Object[] args = {DriveApplication.getContext()};
        BlockBase b;
        try {
            b = constructor.newInstance(args);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return b;
    }

    /**
     * Create a sequence block
     *
     * @param blockName
     * @return
     */
    private static ArrayList<BlockBase> createSequenceBlock(String blockName) {
        ArrayList<BlockBase> blocks = new ArrayList<>();
        BlockBase b = create(blockName);
        blocks.add(b);
        return blocks;
    }

    /**
     * create a repetition block and an end block
     *
     * @param blockName
     * @return
     */
    private static ArrayList<BlockBase> createRepetitionBlock(
            String blockName) {
        ArrayList<BlockBase> blocks = new ArrayList<>();
        BlockBase b = new RepetitionEndBlock(DriveApplication.getContext()); // Add
        // RepetitionEndBlock
        blocks.add(b);
        b = create(blockName);
        blocks.add(b);
        return blocks;
    }

    /**
     * Create a selection block and an end block
     *
     * @param blockName
     * @return
     */
    private static ArrayList<BlockBase> createSelectionBlock(String blockName) {
        ArrayList<BlockBase> blocks = new ArrayList<>();
        BlockBase b = new SelectionEndBlock(DriveApplication.getContext()); // Add RepetitionEndBlock
        blocks.add(b);
        b = create(blockName);
        blocks.add(b);
        return blocks;
    }

    public static ArrayList<BlockBase> createBlocks(int howToMake, String blockName) {
        ArrayList<BlockBase> blocks = null;
        if (howToMake == SEQUENCE) {
            blocks = createSequenceBlock(blockName);
        } else if (howToMake == REPETITION) {
            blocks = createRepetitionBlock(blockName);
        } else if (howToMake == SELECTION) {
            blocks = createSelectionBlock(blockName);
        } else if (howToMake == UNDO || howToMake == LOAD) {
            // When users undo or load their program, this app should create
            // a block. It is the same way when in which app makes sequence
            // block
            blocks = createSequenceBlock(blockName);
        }
        return blocks;
    }
}