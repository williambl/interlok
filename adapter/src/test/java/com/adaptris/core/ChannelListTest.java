/*
 * Copyright 2015 Adaptris Ltd.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

package com.adaptris.core;

import java.util.Arrays;
import java.util.ListIterator;

import org.apache.log4j.Logger;

import com.adaptris.util.IdGenerator;
import com.adaptris.util.PlainIdGenerator;

public class ChannelListTest extends BaseCase {
  private IdGenerator idGenerator;
  private Logger log = Logger.getLogger(this.getClass());

  public ChannelListTest(java.lang.String testName) {
    super(testName);
  }

  @Override
  public void setUp() throws Exception {
    idGenerator = new PlainIdGenerator();
  }

  public void testRemoveThenAddAgain() throws Exception {
    ChannelList channelList = new ChannelList();
    Channel c = new Channel();
    c.setUniqueId(getName());
    channelList.add(c);
    assertEquals(1, channelList.size());
    assertTrue(channelList.removeChannel(c));
    assertEquals(0, channelList.size());
    channelList.add(c);
    assertEquals(1, channelList.size());
  }

  public void testRemoveByObject() throws Exception {
    ChannelList channelList = new ChannelList();
    Channel c = new Channel();
    c.setUniqueId(getName());
    channelList.add(c);
    assertEquals(1, channelList.size());
    assertTrue(channelList.remove(c));
    assertEquals(0, channelList.size());
  }

  public void testRedmine5407() throws Exception {
    testRemoveThenAddAgain();
  }

  public void testRemoveNull() throws Exception {
    ChannelList channelList = new ChannelList();
    assertFalse(channelList.remove(null));
    assertFalse(channelList.removeChannel(null));
  }

  public void testRemovePlainObject() throws Exception {
    ChannelList channelList = new ChannelList();
    assertFalse(channelList.remove(new Object()));

  }

  public void testAddDuplicate() {
    ChannelList channelList = new ChannelList();
    Channel toAdd = new Channel();
    channelList.add(toAdd);
    channelList.add(toAdd);
    assertEquals(2, channelList.size());
  }

  public void testCollectionConstructor() {
    ChannelList channelList = new ChannelList();
    Channel channel1 = new Channel();
    Channel channel2 = new Channel();
    channelList.add(channel1);
    channelList.add(channel2);
    ChannelList channelList2 = new ChannelList(channelList);
    assertEquals(2, channelList2.size());
    assertTrue(channelList2.contains(channel1));
    assertTrue(channelList2.contains(channel1));
  }

  public void testAddAllAtPosition() {
    ChannelList channelList = new ChannelList();
    Channel channel1 = new Channel();
    Channel channel2 = new Channel();
    channelList.add(channel1);
    channelList.add(channel2);
    ChannelList channelList2 = new ChannelList();
    channelList2.add(new Channel());
    channelList2.add(new Channel());
    channelList2.addAll(1, channelList);
    assertEquals(4, channelList2.size());
    assertTrue(channelList2.contains(channel1));
    assertTrue(channelList2.contains(channel1));
  }

  public void testAdd() {
    ChannelList channelList = new ChannelList();
    channelList.add(new Channel());
    assertEquals(1, channelList.size());
    try {
      channelList.addChannel(null);
    }
    catch (IllegalArgumentException expected) {
    }
    assertEquals(1, channelList.size());
  }

  public void testAddAtPosition() {
    ChannelList channelList = new ChannelList();
    channelList.add(new Channel());
    channelList.add(new Channel());
    Channel toAdd = new Channel();
    channelList.add(1, toAdd);

    assertEquals(3, channelList.size());
    assertEquals(toAdd, channelList.get(1));
  }

  public void testGetByInt() {
    ChannelList wl = new ChannelList();
    Channel toAdd = new Channel();
    wl.add(new Channel());
    wl.add(toAdd);
    assertEquals(toAdd, wl.get(1));
  }

  public void testGetChannels() {
    ChannelList wl = new ChannelList();
    wl.add(new Channel());
    assertNotNull(wl.getChannels());
    assertEquals(1, wl.getChannels().size());
  }

  public void testIndexOf() {
    ChannelList wl = new ChannelList();
    wl.add(new Channel());
    wl.add(new Channel());
    Channel toAdd = new Channel();
    wl.add(toAdd);
    assertEquals(3, wl.size());
    assertEquals(2, wl.indexOf(toAdd));
  }

  public void testIterator() {
    ChannelList wl = new ChannelList();
    wl.add(new Channel());
    wl.add(new Channel());
    assertNotNull(wl.iterator());
    assertTrue(wl.iterator().hasNext());
  }

  public void testListIterator_HasPreviousNext() {
    ChannelList list = new ChannelList();
    list.add(new Channel());
    list.add(new Channel());
    list.add(new Channel());
    list.add(new Channel());
    list.add(new Channel());
    int count = 0;
    for (ListIterator<Channel> i = list.listIterator(); i.hasNext();) {
      switch (count) {
      case 0: {
        assertEquals(-1, i.previousIndex());
        assertFalse(i.hasPrevious());
        assertTrue(i.hasNext());
        break;
      }
      case 5: {
        assertEquals(5, i.nextIndex());
        assertTrue(i.hasPrevious());
        assertFalse(i.hasNext());
        break;
      }
      default: {
        assertEquals(count, i.nextIndex());
        assertNotNull(i.previous());
        assertNotNull(i.next());
        assertTrue(i.hasPrevious());
        assertTrue(i.hasNext());
      }
      }
      i.next();
      count++;
    }
  }

  public void testLastIndexOf() {
    ChannelList wl = new ChannelList();
    Channel toAdd = new Channel();
    wl.add(toAdd);
    wl.add(toAdd);
    wl.add(toAdd);
    assertEquals(2, wl.lastIndexOf(toAdd));
  }

  public void testListIterator() {
    ChannelList wl = new ChannelList();
    wl.add(new Channel());
    wl.add(new Channel());
    wl.add(new Channel());
    wl.add(new Channel());
    assertNotNull(wl.listIterator());
    assertNotNull(wl.listIterator(1));
  }

  public void testRemove() {
    ChannelList list = new ChannelList();
    list.add(new Channel());
    list.add(new Channel());
    Channel channel = new Channel();
    channel.setUniqueId("channel");
    list.add(channel);
    list.add(new Channel());
    list.add(new Channel());
    assertEquals(5, list.size());
    list.remove(4); // Remove a channel w/o an id.
    assertEquals(4, list.size());
    list.remove(2);
    assertFalse(list.contains(channel));
    assertNull(list.getChannel("channel"));
    assertEquals(3, list.size());
  }

  public void testSubList() {
    ChannelList list = new ChannelList();
    list.add(new Channel());
    list.add(new Channel());
    list.add(new Channel());
    list.add(new Channel());
    assertNotNull(list.subList(0, 2));
    assertEquals(2, list.subList(0, 2).size());
  }

  public void testAddChannelUniqueIds() throws CoreException {

    Channel ch1 = new Channel();
    Channel ch2 = new Channel();
    ChannelList list = new ChannelList();
    list.addChannel(ch1);
    list.addChannel(ch2);
    Channel ch3 = new Channel();
    ch3.setUniqueId("ch3");
    list.add(ch3);
    try {
      list.add(ch3);
      fail();
    }
    catch (IllegalArgumentException expected) {

    }
    assertEquals(3, list.size());
    Channel ch4 = new Channel();
    ch4.setUniqueId("ch4");
    list.add(ch4);
    assertEquals(4, list.size());
    list.remove(3);
    list.add(ch4);
    assertEquals(4, list.size());
  }

  public void testSetChannels() throws Exception {
    ChannelList list = new ChannelList();
    try {
      list.setChannels(null);
    }
    catch (IllegalArgumentException expected) {
    }
    list.setChannels(Arrays.asList(new Channel[]
    {
      new Channel()
    }));
    assertEquals(1, list.size());
  }

  public void testSetChannelsWithDuplicateID() {
    ChannelList list = new ChannelList();
    try {
      list.setChannels(Arrays.asList(new Channel[]
      {
          new Channel() {
            @Override
            public String getUniqueId() {
              return "channel1";
            }
          }, new Channel() {
            @Override
            public String getUniqueId() {
              return "channel1";
            }
          }
      }));
      fail();
    }
    catch (IllegalArgumentException expected) {

    }
  }

  public void testAddAllChannelsWithDuplicateID() {
    ChannelList list = new ChannelList();
    try {
      list.addAll(Arrays.asList(new Channel[]
      {
          new Channel() {
            @Override
            public String getUniqueId() {
              return "channel1";
            }
          }, new Channel() {
            @Override
            public String getUniqueId() {
              return "channel1";
            }
          }
      }));
      fail();
    }
    catch (IllegalArgumentException expected) {

    }
  }

  public void testSetByInt() {
    ChannelList list = new ChannelList();
    list.add(new Channel());
    list.add(new Channel() {
      @Override
      public String getUniqueId() {
        return "replacedChannel";
      }
    });
    list.add(new Channel());
    Channel toAdd = new Channel();
    list.set(1, toAdd);
    assertEquals(toAdd, list.get(1));
    assertNull(list.getChannel("replacedChannel"));
  }

  public void testGetChannelByUniqueId() throws Exception {
    ChannelList list = new ChannelList();
    list.setChannels(Arrays.asList(new Channel[]
    {
        new Channel() {
          @Override
          public String getUniqueId() {
            return "channel1";
          }
        }, new Channel() {
          @Override
          public String getUniqueId() {
            return "channel2";
          }
        }

    }));
    assertEquals(2, list.size());
    assertNotNull(list.getChannel("channel1"));
    assertNotNull(list.getChannel("channel2"));
    assertNull(list.getChannel("channel99"));
    try {
      list.getChannel("");
      fail();
    }
    catch (IllegalArgumentException expected) {

    }
    try {
      list.getChannel(null);
      fail();
    }
    catch (IllegalArgumentException expected) {

    }
  }

  public void testGetChannelByPosition() throws Exception {
    ChannelList list = new ChannelList();
    list.setChannels(Arrays.asList(new Channel[]
    {
        new Channel() {
          @Override
          public String getUniqueId() {
            return "channel1";
          }
        }, new Channel() {
          @Override
          public String getUniqueId() {
            return "channel2";
          }
        }

    }));
    assertEquals(2, list.size());
    assertNotNull(list.getChannel(0));
    try {
      list.getChannel(15);
    }
    catch (ArrayIndexOutOfBoundsException expected) {
      ;
    }
    try {
      list.getChannel(-1);
    }
    catch (ArrayIndexOutOfBoundsException expected) {
      ;
    }
  }

  public void testChannelAutoStartFalse() throws Exception {
    ChannelList list = new ChannelList();
    Channel testChannel = new Channel();
    testChannel.setUniqueId("testAutoStart");
    testChannel.setAutoStart(Boolean.FALSE);
    list.setChannels(Arrays.asList(new Channel[]
    {
      testChannel
    }));
    list.prepare();
    start(list);
    assertEquals(ClosedState.getInstance(), testChannel.retrieveComponentState());
  }

  public void testChannelAutoStartTrue() throws Exception {
    ChannelList list = new ChannelList();
    Channel testChannel = new Channel();
    testChannel.setUniqueId("testAutoStart");
    testChannel.setAutoStart(Boolean.TRUE);
    list.setChannels(Arrays.asList(new Channel[]
    {
      testChannel
    }));
    list.prepare();
    start(list);
    assertEquals(StartedState.getInstance(), testChannel.retrieveComponentState());
  }

  // No ID means that we always start regardless of auto-start being false.
  public void testChannelAutoStartFalseNoUniqueId() throws Exception {
    ChannelList list = new ChannelList();
    Channel testChannel = new Channel();
    testChannel.setAutoStart(Boolean.FALSE);
    list.setChannels(Arrays.asList(new Channel[]
    {
      testChannel
    }));
    list.prepare();
    start(list);
    assertEquals(StartedState.getInstance(), testChannel.retrieveComponentState());
  }

  public void testChannelAutoStartTrueNoUniqueId() throws Exception {
    ChannelList list = new ChannelList();
    Channel testChannel = new Channel();
    testChannel.setAutoStart(Boolean.TRUE);
    list.setChannels(Arrays.asList(new Channel[]
    {
      testChannel
    }));
    list.prepare();
    start(list);
    assertEquals(StartedState.getInstance(), testChannel.retrieveComponentState());
  }

  public void testChannelAutoStartNotSpecified() throws Exception {
    ChannelList list = new ChannelList();
    Channel testChannel = new Channel();
    list.setChannels(Arrays.asList(new Channel[]
    {
      testChannel
    }));
    list.prepare();
    start(list);
    assertEquals(StartedState.getInstance(), testChannel.retrieveComponentState());
  }

}
