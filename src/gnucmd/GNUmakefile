# $Id: GNUmakefile,v 1.6 2008/03/17 14:38:55 am143972 Exp $

# GPL Notice
#      This program is free software; you can redistribute it and/or modify
#      it under the terms of the GNU General Public License as published by
#      the Free Software Foundation; version 2.
#
#      This program is distributed in the hope that it will be useful,
#      but WITHOUT ANY WARRANTY; without even the implied warranty of
#      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#      GNU General Public License for more details.
#
#      You should have received a copy of the GNU General Public License
#      along with this program; if not, write to the Free Software Foundation,
#      Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

#      For the avoidance of doubt, except that if any license choice other
#      than GPL or LGPL is available it will apply instead, Sun elects to
#      use only the General Public License version 2 (GPLv2) at this time
#      for any software where a choice of GPL license versions is made
#      available with the language indicating that GPLv2 or any later
#      version may be used, or where a choice of which version of the GPL
#      is applied is otherwise unspecified.

DEPTH = ../..

include $(DEPTH)/mk/common.mk

DIRS = 	fileutils-3.16

ifeq ($(OS), SunOS)
DIRS +=	tar-1.12 
endif

include $(DEPTH)/mk/targets.mk

all:
	@chmod -w fileutils-3.16/lib/$(OBJ_DIR)/getdate.c
	@chmod -w fileutils-3.16/lib/$(OBJ_DIR)/posixtm.c
ifeq ($(OS), SunOS)
	@chmod -w tar-1.12/lib/$(OBJ_DIR)/getdate.c
endif

include $(DEPTH)/mk/depend.mk
